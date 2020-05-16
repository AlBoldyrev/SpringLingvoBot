package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageNew implements IResponseHandler {

    private final UserInfoService userInfoService;
    private final UserDialogRepository userDialogRepository;
    private final DialogRepository dialogRepository;
    private final UserRepository userRepository;
    private final GroupActor groupActor;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserService userService;
    private final MessageService messageService;
    private final NodeRepository nodeRepository;
    private final NodeNextRepository nodeNextRepository;
    private final UserPhraseRepository userPhraseRepository;
    private final PhraseRepository phraseRepository;
    private static String messageBody;
    private final DialogService dialogService;
    private final PhraseService phraseService;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        MessageNew.messageBody = message.getObject().getBody();
        User user = userInfoService.isExists(userVkId);

        if (user == null) {
            user = userService.createNewUser(userVkId);
        }

        if (user.getLevel().equals(MenuLevel.MAIN.getCode())) {
            if (messageBody.equals("Phrases")) {
                user.setLevel(MenuLevel.PHRASE.getCode());
                userRepository.save(user);
            } else if (messageBody.equals("Dialogs")) {
                user.setLevel(MenuLevel.DIALOGS.getCode());
                userRepository.save(user);
            } else {
                actionLevel1(userVkId);
            }
        }

        if (user.getLevel().equals(MenuLevel.DIALOGS.getCode())) {
            if (messageBody.equals("BACK")) {
                user.setLevel(MenuLevel.MAIN.getCode());
                userRepository.save(user);
                actionLevel1(userVkId);
            } else {
                UserDialog currentDialogOfUser = userDialogRepository.findCurrentDialogOfUser(user.getUserId());
                if (currentDialogOfUser == null) {
                    dialogService.actionLevel2(userVkId, messageBody);
                } else {
                    dialogService.proceedTheDialog(currentDialogOfUser.getDialog().getDialogName(), user.getVkId(), messageBody);
                }
            }
        }

        if (user.getLevel().equals(MenuLevel.PHRASE.getCode())) {

            List<String> icons = new ArrayList<>();
            String Ru_Eng = new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA))
                    + "->" + new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7));
            String Eng_Ru = new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7))
                    + "->" + new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA));
            String BACK = "BACK";
            icons.add(Ru_Eng);
            icons.add(Eng_Ru);
            icons.add(BACK);



            if (messageBody.equals("BACK")) {
                user.setLevel(MenuLevel.MAIN.getCode());
                userRepository.save(user);
                actionLevel1(userVkId);
            } else if (messageBody.equals(Eng_Ru)) {
                user.setLevel(MenuLevel.ENG_RU.getCode());
                userRepository.save(user);
            } else if (messageBody.equals(Ru_Eng)) {
                user.setLevel(MenuLevel.RU_ENG.getCode());
                userRepository.save(user);
            } else {
                Keyboard keyboardWithIcons = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(icons);
                messageService.sendMessageWithTextAndKeyboard(userVkId, "Выберите режим!", keyboardWithIcons);
            }
        }

        if (user.getLevel().equals(MenuLevel.ENG_RU.getCode())) {

            if (messageBody.equals("BACK")) {
                user.setLevel(MenuLevel.MAIN.getCode());
                userRepository.save(user);
                actionLevel1(userVkId);
            }

            phraseService.actionLevel4(userVkId);
        }

        if (user.getLevel().equals(MenuLevel.RU_ENG.getCode())) {

            if (messageBody.equals("BACK")) {
                user.setLevel(MenuLevel.MAIN.getCode());
                userRepository.save(user);
                actionLevel1(userVkId);
            }

            phraseService.actionLevel5(userVkId);
        }


    }

    private void actionLevel1(int userVkId) {
        List<String> levelFirst = new ArrayList<>();
        levelFirst.add("Phrases");
        levelFirst.add("Dialogs");
        Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(levelFirst);
        messageService.sendMessageWithTextAndKeyboard(userVkId, "Дружище, ты в главном меню.", keyboardWithButtonsBrickByBrick);
    }




}