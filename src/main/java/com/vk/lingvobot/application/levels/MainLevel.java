package com.vk.lingvobot.application.levels;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.levels.dialog.DialogLevelOne;
import com.vk.lingvobot.application.levels.phrase.levelOne.PhraseLevelOne;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainLevel implements IResponseMessageBodyHandler {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final DialogLevelOne dialogLevelOne;
    private final PhraseLevelOne phraseLevelOne;

    @Override
    public void handle(User user, String messageBody) {

        if (messageBody.equals("Phrases")) {
            user.setLevel(MenuLevel.PHRASE.getCode());
            userRepository.save(user);
            phraseLevelOne.handle(user, messageBody);
        } else if (messageBody.equals("Dialogs")) {
            user.setLevel(MenuLevel.DIALOGS.getCode());
            userRepository.save(user);
            dialogLevelOne.handle(user, messageBody);
        } else {
            actionLevel1(user.getVkId());
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
