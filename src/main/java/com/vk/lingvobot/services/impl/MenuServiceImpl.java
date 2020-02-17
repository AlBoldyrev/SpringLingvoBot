package com.vk.lingvobot.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.entities.menu.MenuLevel;
import com.vk.lingvobot.entities.menu.MenuStage;
import com.vk.lingvobot.keyboard.CustomButton;
import com.vk.lingvobot.keyboards.MenuButtons;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.LinkData;
import com.vk.lingvobot.parser.importDialogParser.NodeData;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.MenuStageRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MenuService;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.PhrasePairStateService;
import com.vk.lingvobot.services.UserDialogService;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MessageServiceKt messageService;
    private final MenuStageRepository menuStageRepository;
    private final DialogRepository dialogRepository;
    private final UserDialogRepository userDialogRepository;
    private final UserDialogService userDialogService;
    private final PhrasePairStateService phrasePairStateService;

    private final Map<String, String> dialogsNames = new HashMap<>();
    private Gson gson = new GsonBuilder().create();


    private List<List<CustomButton>> mainMenuButtons = new ArrayList<>();
    private List<CustomButton> buttons = new ArrayList<>();

    @PostConstruct
    private void init() {
        buttons.add(new CustomButton(MenuButtons.PHRASES.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        buttons.add(new CustomButton(MenuButtons.DIALOGS.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        buttons.add(new CustomButton(MenuButtons.IMPORT_DIALOGS.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        mainMenuButtons.add(buttons);
    }

    @Override
    public void processMainDialog(User user, GroupActor groupActor, List<String> buttonList) {

    }

    @Override
    public void handle(User user, String messageBody, GroupActor groupActor) {
        MenuStage menuStage = menuStageRepository.findByUser(user.getUserId());

        if (menuStage == null) {
            menuStage = new MenuStage();
            menuStage.setUser(user);
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStage.setCurrentDialogPage(0);
            menuStage = menuStageRepository.save(menuStage);
        }

        switch (menuStage.getMenuLevel()) {
            case MAIN:
                callMainMenu(user, messageBody, menuStage, groupActor);
                break;
            case DIALOG:
                callDialogMenu(user, messageBody, menuStage, groupActor);
                break;
            case PHRASE:
            case PHRASE_RUS_ENG:
            case PHRASE_ENG_RUS:
                callPhraseMenu(user, messageBody, menuStage, groupActor);
                break;
            case IMPORT_DIALOG:
                callImportDialogProcess(user, messageBody, menuStage, groupActor);
                break;
        }
    }

    private void callMainMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        if (messageBody.equals(MenuButtons.DIALOGS.getValue())) {
            menuStage.setMenuLevel(MenuLevel.DIALOG);
            menuStageRepository.save(menuStage);
            callDialogMenu(user, messageBody, menuStage, groupActor);
        } else if (messageBody.equals(MenuButtons.PHRASES.getValue())) {
            menuStage.setMenuLevel(MenuLevel.PHRASE);
            menuStageRepository.save(menuStage);
            callPhraseMenu(user, messageBody, menuStage, groupActor);
        } else if (messageBody.equals(MenuButtons.IMPORT_DIALOGS.getValue())) {
            menuStage.setMenuLevel(MenuLevel.IMPORT_DIALOG);
            menuStageRepository.save(menuStage);
            callImportDialogProcess(user, messageBody, menuStage, groupActor);
        } else {
            messageService.sendMessageWithTextAndKeyboard(
                    groupActor, user.getVkId(), "Выберите режим обучения", mainMenuButtons);
        }
    }

    private void callImportDialogProcess(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {

        importDialog();

        if (messageBody.equals(MenuButtons.IMPORT_DIALOGS.getValue())) {
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStageRepository.save(menuStage);
            messageService.sendMessageWithTextAndKeyboard(
                    groupActor, user.getVkId(), "Выберите режим обучения", mainMenuButtons);
        }

    }

    private void importDialog() {

        System.out.println("Import............................");
        Path path = Paths.get("C:/Work/test.txt");
        byte[] bytes = new byte[0];

        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException ioe) {
            log.error("IOException while reading file from disk. " + ioe.getStackTrace());
        }

        String string = null;

        try {
            string = new String(bytes,"Cp1251");
        } catch (UnsupportedEncodingException e) {
            log.error("File can not be encoded to cp1251. Too bad. " + e.getStackTrace());
        }

        ImportDialogParser importDialogData = gson.fromJson(string, ImportDialogParser.class);

        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogData);
        findAllConnections(importDialogDataWithPositiveValues);
        detectAllRoundedRectanglesWithRelations(importDialogDataWithPositiveValues);
        squashAllKeyboardsCandidates(importDialogData);

    }

    private ImportDialogParser convertNodesIntoPositiveValues(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        nodeDataList.forEach(s -> s.setKey(Math.abs(s.getKey())));
        linkDataList.forEach(s -> s.setFrom(Math.abs(s.getFrom())));
        linkDataList.forEach(s -> s.setTo(Math.abs(s.getTo())));

        importDialogParser.setLinkDataList(linkDataList);
        importDialogParser.setNodeDataList(nodeDataList);

        return importDialogParser;
    }

    /**
     *  f.e. if (3,4,5) - rectangular objects then (1,3), (1,4), (1,5), (3,6), (4,6), (5,6) --> (1,6)
     * @param importDialogParser
     * @return
     */
    private ImportDialogParser squashAllKeyboardsCandidates(ImportDialogParser importDialogParser) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogParser);
        List<Map.Entry<Integer, List<Integer>>> entries = detectAllRoundedRectanglesWithRelations(importDialogDataWithPositiveValues);
        for (Map.Entry<Integer, List<Integer>> entry : entries) {
            List<Integer> value = entry.getValue();
            for (Integer it : value) {
                List<Integer> specificNodeConnectionsToAndFrom = findSpecificNodeConnectionsToAndFrom(importDialogParser, it);
                System.out.println();
                if (specificNodeConnectionsToAndFrom.size() == 2) {

                    LinkData newLink = new LinkData(specificNodeConnectionsToAndFrom.get(0), specificNodeConnectionsToAndFrom.get(1));
                    LinkData oldLinkOneSide = new LinkData(specificNodeConnectionsToAndFrom.get(0), it);
                    LinkData oldLinkOtherSide = new LinkData(it, specificNodeConnectionsToAndFrom.get(1));

                    boolean first = linkDataList.contains(oldLinkOneSide);
                    boolean second = linkDataList.contains(oldLinkOtherSide);

                    System.out.println("first = " + first + " and second = " + second);
                    System.out.println("Element with connection " + specificNodeConnectionsToAndFrom.get(0) + " --> " +specificNodeConnectionsToAndFrom.get(1) + "has been added to linkData");

                    linkDataList.add(newLink);
                    linkDataList.remove(oldLinkOneSide);
                    linkDataList.remove(oldLinkOtherSide);


                }
            }
        }
        importDialogParser.setLinkDataList(linkDataList);
        return importDialogParser;

    }

    private void findAllConnections(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        for (NodeData nodeData : nodeDataList) {
            int key = nodeData.getKey();
            List<Integer> connections = new ArrayList<>();
            for (LinkData linkData : linkDataList) {
                int from = linkData.getFrom();
                if (key == from) {
                    int to = linkData.getTo();
                    connections.add(to);
                }
            }
            System.out.println("Node " + key + " is connected with node: " + String.join("," , connections.toString()));
        }
    }

    private List<Integer> findSpecificNodeConnectionsToAndFrom(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        List<Integer> connections = new ArrayList<>();

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == from) {
                connections.add(to);
            }
            if (nodeKey == to) {
                connections.add(from);
            }
        }
        return connections;
    }

    private Integer findSpecificNodeConnectionsOnlyFrom(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Integer connections = 0;

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == to) {
                connections = from;

            }
        }
        return connections;
    }



    private List<Map.Entry<Integer, List<Integer>>> detectAllRoundedRectanglesWithRelations(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Map<Integer, Integer> correlationsBetweenNodes = new HashMap<>();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                List<Integer> specificNodeConnections = findSpecificNodeConnectionsToAndFrom(importDialogParser, nodeData.getKey());
                Integer specificNodeConnectionsOnlyTo = findSpecificNodeConnectionsOnlyFrom(importDialogParser, nodeData.getKey());
                System.out.println("candidates for keyboard: " + nodeData.getKey() + " . This node is connected with: " + specificNodeConnectionsOnlyTo);

                correlationsBetweenNodes.put(nodeData.getKey(), specificNodeConnectionsOnlyTo);
            }
        }

        List<Map.Entry<Integer, List<Integer>>> collect = correlationsBetweenNodes.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toList());

        correlationsBetweenNodes.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .forEach(System.out::println);
        System.out.println(";sdsdsd" + collect.toString());
        return collect;
    }


    private List<Integer> detectAllRoundedRectanglesNodes(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        List<Integer> nodesKeys = new ArrayList<>();
        for (NodeData nodeData : nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                nodesKeys.add(nodeData.getKey());
            }
        }
        return nodesKeys;
    }
/*    private void groupKeyboardCandidateNode(ImportDialogParser importDialogParser) {

    }*/


    private void callDialogMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        List<Dialog> allDialogs = dialogRepository.findAllDialogExceptSettingOne();

        if (messageBody.equals(MenuButtons.NEXT.getValue())) {
            int nextDialogPage = menuStage.getCurrentDialogPage() + 1;
            sendDialogsKeyboard(user, nextDialogPage, groupActor);
            menuStage.setCurrentDialogPage(nextDialogPage);
            menuStageRepository.save(menuStage);
        } else if (messageBody.equals(MenuButtons.BACK.getValue())) {
            int previousDialogPage = menuStage.getCurrentDialogPage() - 1;
            sendDialogsKeyboard(user, previousDialogPage, groupActor);
            menuStage.setCurrentDialogPage(previousDialogPage);
            menuStageRepository.save(menuStage);
        } else if (messageBody.equals(MenuButtons.HOME.getValue())) {
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStageRepository.save(menuStage);
            callMainMenu(user, messageBody, menuStage, groupActor);
        } else {
            String savedDialogName = dialogsNames.get(messageBody);
            if (savedDialogName != null && !savedDialogName.isEmpty()) {
                enterTheDialog(user, savedDialogName, groupActor);
                userDialogService.processCommonDialog(user, groupActor);
            } else {
                sendDialogsKeyboard(user, 0, groupActor);
                menuStage.setCurrentDialogPage(0);
                menuStageRepository.save(menuStage);
            }
        }

    }

    private void callPhraseMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        enterTheDialog(user, messageBody, groupActor);
        phrasePairStateService.phrasesDialogStart(user);
        userDialogService.processPhrasesPairDialog(user, groupActor, messageBody);
    }

    private void sendDialogsKeyboard(User user, int pageNumber, GroupActor groupActor) {
        int pageSize = 5;
        StringBuilder message = new StringBuilder();
        message.append("Выберите один из диалогов:\n");
        List<UserDialog> allUserDialogs = userDialogRepository.findAllUserDialogs(user.getUserId());

        List<List<CustomButton>> allButtons = new ArrayList<>();
        List<CustomButton> buttonsInRow = new ArrayList<>();
        List<CustomButton> navigationButtons = new ArrayList<>();

        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("dialogId").ascending());
        Page<Dialog> dialogsPage = dialogRepository.findAllDialogExceptSettingOne(page);

        int maxPageElementNumber = pageSize * (dialogsPage.getNumber() + 1);
        int pageStartElementNumber = maxPageElementNumber - (pageSize - 1);

        if (!dialogsPage.getContent().isEmpty()) {
            for (Dialog dialog : dialogsPage) {
                String label = "*" + pageStartElementNumber + "*";
                UserDialog foundUserDialog = allUserDialogs.stream().filter(userDialog -> userDialog.getDialog().equals(dialog)).findAny().orElse(null);
                if (foundUserDialog != null && foundUserDialog.getIsFinished()) {
                    buttonsInRow.add(new CustomButton(/*dialog.getDialogName()*/ label,
                            KeyboardButtonActionType.TEXT,
                            KeyboardButtonColor.POSITIVE,
                            ""));
                } else {
                    buttonsInRow.add(new CustomButton(/*dialog.getDialogName()*/ label,
                            KeyboardButtonActionType.TEXT,
                            KeyboardButtonColor.DEFAULT,
                            ""));
                }
                dialogsNames.put(label, dialog.getDialogName());
                message.append(pageStartElementNumber).append(": ").append(dialog.getDialogName()).append("\n");
                pageStartElementNumber += 1;
            }
            allButtons.add(buttonsInRow);
        }

        if (dialogsPage.isFirst()) {
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            if (dialogsPage.hasNext()) {
                navigationButtons.add(new CustomButton(MenuButtons.NEXT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            }
        } else if (dialogsPage.isLast()) {
            navigationButtons.add(new CustomButton(MenuButtons.BACK.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        } else {
            navigationButtons.add(new CustomButton(MenuButtons.BACK.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.NEXT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        }

        allButtons.add(navigationButtons);

        messageService.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(), message.toString(), allButtons);
    }

    /**
     * User sends us name of the particular dialog via Keyboard and we create UserDialog object using this data
     */
    private void enterTheDialog(User user, String message, GroupActor groupActor) {
        Dialog dialog = dialogRepository.findByDialogName(message);
        if (dialog == null) {
            MenuStage menuStage = menuStageRepository.findByUser(user.getUserId());
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStageRepository.save(menuStage);
            callMainMenu(user, "default", menuStage, groupActor);
            log.error("dialog with unexisting name");
        } else {
            UserDialog userDialog = new UserDialog(user, dialog, false, false, false);
            userDialog.setState(1);
            userDialogService.create(userDialog);
        }
    }
}
