package com.vk.lingvobot.application.levels;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserPhrase;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.repositories.UserPhraseRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.PhraseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Menu {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhraseService phraseService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CustomJavaKeyboard customJavaKeyboard;
    @Autowired
    private UserPhraseRepository userPhraseRepository;

    protected void baseMenuAction(User user) {
        List<String> levelFirstButtonNames = new ArrayList<>();
        levelFirstButtonNames.add("Phrases");
        levelFirstButtonNames.add("Dialogs");
        levelFirstButtonNames.add("Import dialog");

        UserPhrase userPhrase = userPhraseRepository.findByUserId(user.getUserId());
        if (userPhrase != null) {
            userPhrase.setIsFinished(true);
            userPhraseRepository.save(userPhrase);
        }

        Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(levelFirstButtonNames);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(), "Дружище, ты в главном меню.", keyboardWithButtonsBrickByBrick);
    }

    protected void setTheLevel(User user, Integer level) {
        user.setLevel(level);
        userRepository.save(user);
    }
}
