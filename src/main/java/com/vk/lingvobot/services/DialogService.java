package com.vk.lingvobot.services;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.CustomJavaButton;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DialogService {

    private final DialogRepository dialogRepository;
    private final UserDialogRepository userDialogRepository;
    private final UserService userService;
    private final NodeRepository nodeRepository;
    private final NodeNextRepository nodeNextRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserRepository userRepository;


    public void actionLevel2(int userVkId, String dialogName) {

        User user = userService.findByVkId(userVkId);
        //TODO lambda
        List<Dialog> allDialogs = dialogRepository.findAllDialogs();
        List<String> allDialogNames = new ArrayList<>();
        for (Dialog dialog : allDialogs) {
            allDialogNames.add(dialog.getDialogName());
        }

        if (allDialogNames.contains(dialogName)) {
            proceedTheDialog(dialogName, userVkId, dialogName);

        } else {


            int pageSize = 5;
            Integer pageNumber = user.getPage();
            if (pageNumber == null) {
                pageNumber = 0;
            }
            if (dialogName.equals("->")) {
                user.setPage(++pageNumber);
                userService.save(user);
            }
            if (dialogName.equals("<-")) {
                user.setPage(--pageNumber);
                userService.save(user);
            }
            Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("dialogId").ascending());
            Page<Dialog> allPageDialogs = dialogRepository.findAllPageDialogs(page);

            List<String> dialogNamesPage = new ArrayList<>();
            for (Dialog dialog: allPageDialogs) {
                dialogNamesPage.add(dialog.getDialogName());
            }


            int maxPageElementNumber = pageSize * (allPageDialogs.getNumber() + 1);
            int pageStartElementNumber = maxPageElementNumber - (pageSize - 1);

            //TODO lambda
            List<String> finishedDialogs = new ArrayList<>();
            List<UserDialog> finishedDialogByUserId = userDialogRepository.findFinishedDialogByUserId(user.getUserId());
            for (UserDialog userDialog: finishedDialogByUserId) {
                Dialog dialog = userDialog.getDialog();
                String dialogName2 = dialog.getDialogName();
                finishedDialogs.add(dialogName2);
            }

            List<CustomJavaButton> customJavaButtons = new ArrayList<>();
            for (String dialog: dialogNamesPage) {
                CustomJavaButton customJavaButton = new CustomJavaButton(dialog, "");
                if (finishedDialogs.contains(dialog)) {
                    customJavaButton.setColor(KeyboardButtonColor.POSITIVE);
                }
                customJavaButtons.add(customJavaButton);
            }

            List<String> keyboardNavigationButtons = new ArrayList<>();
            if (allPageDialogs.isFirst() ) {
                    keyboardNavigationButtons.add("BACK");

                String right = "->";
                keyboardNavigationButtons.add(right);


            } else if (allPageDialogs.isLast()) {
                String left = "<-";
                keyboardNavigationButtons.add(left);
                if (!keyboardNavigationButtons.contains("BACK")) {
                    keyboardNavigationButtons.add("BACK");
                }
            } else {
                String right = "->";
                String left = "<-";
                keyboardNavigationButtons.add(left);
                if (!keyboardNavigationButtons.contains("BACK")) {
                    keyboardNavigationButtons.add("BACK");
                }
                keyboardNavigationButtons.add(right);
            }

            Keyboard keyboardWithNavigationButtons = customJavaKeyboard.createKeyboardWithNavigationButtons(customJavaButtons, keyboardNavigationButtons);

            messageService.sendMessageWithTextAndKeyboard(userVkId, "Выберите нужный диалог:", keyboardWithNavigationButtons);
        }
    }

    public Dialog findById(Integer id) {
        Dialog dialog = dialogRepository.findByDialogId(id);
        if (dialog == null) {
            log.error("There is no dialog with id: " + id);
            return null;
        }
        return dialog;
    }

    public List<Dialog> getAllDialogs() {
        return dialogRepository.findAllDialogs();
    }

    public Dialog getDialogViaName(String name) {
        return dialogRepository.findByDialogName(name);
    }

    public boolean isUserInDialogNow(Integer userId) {

        UserDialog currentDialogOfUser = userDialogRepository.findCurrentDialogOfUser(userId);
        return currentDialogOfUser != null;
    }

    public void proceedTheDialog(String dialogName, int userVkId, String messageBody) {
        User user = userService.findByVkId(userVkId);
        Integer nodeKey;
        UserDialog currentDialogOfUser = userDialogRepository.findCurrentDialogOfUser(user.getUserId());
        if (currentDialogOfUser == null) {
            currentDialogOfUser = startTheDialog(dialogName, user);
            nodeKey = currentDialogOfUser.getNodeId();
        } else {
            nodeKey = findNodeWhereUserEnded(dialogName, user);
        }
        NodeNext reallyNextNode = null;
        try {
            Node byNodeId = nodeRepository.findByNodeKey(nodeKey, currentDialogOfUser.getDialog().getDialogId());
            reallyNextNode = nodeNextRepository.findByKeyboardValue(messageBody, byNodeId.getNodeKey());
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
        }
        Node node;
        if (reallyNextNode != null) {

            node = nodeRepository.findByNodeKey(reallyNextNode.getNextNode(), currentDialogOfUser.getDialog().getDialogId());
            currentDialogOfUser.setNodeId(node.getNodeKey());
            userDialogRepository.save(currentDialogOfUser);

            nodeKey = reallyNextNode.getNextNode();

        }


        node = nodeRepository.findByNodeKey(nodeKey,currentDialogOfUser.getDialog().getDialogId());

        List<NodeNext> nextNodesCandidates = nodeNextRepository.findByNodeIdNextNodes(nodeKey);



        List<String> keyboardValuesForNode = new ArrayList<>();
        for (NodeNext nodeNext: nextNodesCandidates) {
            String keyboardValue = nodeNext.getKeyboardValue();
            keyboardValuesForNode.add(keyboardValue);
        }

        boolean nonNullElemExist= false;
        for (String s: keyboardValuesForNode) {
            if (s != null) {
                nonNullElemExist = true;
                break;
            }
        }

        if (keyboardValuesForNode.isEmpty() || !nonNullElemExist) {
            messageService.sendMessageTextOnly(userVkId, node.getNodeValue());

            if (nextNodesCandidates.isEmpty()) {
                System.out.println("ДА ВРОДЕ БЫ И ВСЁ!");
                currentDialogOfUser.setIsFinished(true);
                if (currentDialogOfUser.getDialog().getDialogName().equals("GreetingDialog")) {
                    user.setLevel(1);
                    userService.save(user);
                }
                userDialogRepository.save(currentDialogOfUser);
            }
            NodeNext nodeNext = nextNodesCandidates.get(0);
            currentDialogOfUser.setNodeId(nodeNext.getNextNode());
            userDialogRepository.save(currentDialogOfUser);
        } else {
            Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(keyboardValuesForNode);
            messageService.sendMessageWithTextAndKeyboard(userVkId, node.getNodeValue(), keyboardWithButtonsBrickByBrick);
        }


















      /*  NodeNext reallyNextNode = nodeNextRepository.findByKeyboardValue(messageBody);

        if (reallyNextNode != null) {
            currentDialogOfUser.setNodeId(reallyNextNode.getNextNode());
            userDialogRepository.save(currentDialogOfUser);
            nodeKey = reallyNextNode.getNextNode();
        }

        Node node = nodeRepository.findByNodeKey(nodeKey);

        String nodeValue = node.getNodeValue();
        List<NodeNext> nextNodes = nodeNextRepository.findByNodeIdNextNodes(nodeKey);


        //TODO lambdas
        List<String> keyboardValuesForNode = new ArrayList<>();
        for (NodeNext nodeNext: nextNodes) {
            String keyboardValue = nodeNext.getKeyboardValue();
            keyboardValuesForNode.add(keyboardValue);
        }

        boolean nonNullElemExist= false;
        for (String s: keyboardValuesForNode) {
            if (s != null) {
                nonNullElemExist = true;
                break;
            }
        }

        if (keyboardValuesForNode.isEmpty() || !nonNullElemExist) {
            Integer nextNode = nextNodes.get(0).getNextNode();
            Node next = nodeRepository.findByNodeKey(nextNode);
            messageService.sendMessageTextOnly(userVkId, next.getNodeValue());

            List<NodeNext> nextNod = nodeNextRepository.findByNodeIdNextNodes(nextNode);

            currentDialogOfUser.setNodeId(nextNod.get(0).getNodeId());
            userDialogRepository.save(currentDialogOfUser);
        } else {
            Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(keyboardValuesForNode);
            messageService.sendMessageWithTextAndKeyboard(userVkId, nodeValue, keyboardWithButtonsBrickByBrick);
        }

        if (nextNodes.isEmpty()) {
            System.out.println("ДА ВРОДЕ БЫ И ВСЁ!");
            currentDialogOfUser.setIsFinished(true);
            userDialogRepository.save(currentDialogOfUser);
            actionLevel2(userVkId, "");
        } else {
            if (reallyNextNode != null) {
                currentDialogOfUser.setNodeId(reallyNextNode.getNextNode());
            }
        }
        userDialogRepository.save(currentDialogOfUser);*/

    }

    private UserDialog startTheDialog(String dialogName, User user) {
        Dialog dialog = dialogRepository.findByDialogName(dialogName);
        UserDialog userDialog = new UserDialog();
        userDialog.setIsFinished(false);
        userDialog.setUser(user);
        userDialog.setIsCancelled(false);
        userDialog.setDialog(dialog);
        //TODO set START node in DB when parsing
        userDialog.setNodeId(0);
        userDialogRepository.save(userDialog);

        return userDialog;
    }

    private Integer findNodeWhereUserEnded(String dialogName, User user) {
        UserDialog currentDialogOfUser = userDialogRepository.findCurrentDialogOfUser(user.getUserId());
        return currentDialogOfUser.getNodeId();
    }

}
