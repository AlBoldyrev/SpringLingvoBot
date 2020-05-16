package com.vk.lingvobot.services;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.CustomJavaButton;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.NodeNextRepository;
import com.vk.lingvobot.repositories.NodeRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DialogService {

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private UserDialogRepository userDialogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private NodeNextRepository nodeNextRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomJavaKeyboard customJavaKeyboard;


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
            allDialogNames.add("BACK");



            //TODO lambda
            List<String> finishedDialogs = new ArrayList<>();
            List<UserDialog> finishedDialogByUserId = userDialogRepository.findFinishedDialogByUserId(user.getUserId());
            for (UserDialog userDialog: finishedDialogByUserId) {
                Dialog dialog = userDialog.getDialog();
                String dialogName2 = dialog.getDialogName();
                finishedDialogs.add(dialogName2);
            }

            List<CustomJavaButton> customJavaButtons = new ArrayList<>();
            for (String dialog: allDialogNames) {
                CustomJavaButton customJavaButton = new CustomJavaButton(dialog, "");
                if (finishedDialogs.contains(dialog)) {
                    customJavaButton.setColor(KeyboardButtonColor.POSITIVE);
                }
                customJavaButtons.add(customJavaButton);
            }
            List<KeyboardButton> keyboardButtons = customJavaKeyboard.convertCustomJavaButtonIntoKeyboardButtons(customJavaButtons);
            Keyboard keyboardFromKeyboardButtons = customJavaKeyboard.createKeyboardFromKeyboardButtons(keyboardButtons);

            messageService.sendMessageWithTextAndKeyboard(userVkId, "Выберите нужный диалог:", keyboardFromKeyboardButtons);
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
            Node byNodeId = nodeRepository.findByNodeKey(nodeKey);
            reallyNextNode = nodeNextRepository.findByKeyboardValue(messageBody, byNodeId.getNodeKey());
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error(e.getMessage());
        }
        Node node;
        if (reallyNextNode != null) {

            node = nodeRepository.findByNodeKey(reallyNextNode.getNextNode());
            currentDialogOfUser.setNodeId(node.getNodeKey());
            userDialogRepository.save(currentDialogOfUser);

            nodeKey = reallyNextNode.getNextNode();

        }


        node = nodeRepository.findByNodeKey(nodeKey);

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
