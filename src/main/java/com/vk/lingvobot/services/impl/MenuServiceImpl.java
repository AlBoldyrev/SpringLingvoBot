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
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.MenuStageRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MenuService;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.PhrasePairStateService;
import com.vk.lingvobot.services.UserDialogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println("string: " + string);

        ImportDialogParser importDialogData = gson.fromJson(string, ImportDialogParser.class);
        System.out.println(importDialogData.toString());

    }

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
            UserDialog userDialog = new UserDialog(user, dialog, false, false);
            userDialog.setState(1);
            userDialogService.create(userDialog);
        }
    }
}
