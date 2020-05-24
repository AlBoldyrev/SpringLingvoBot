package com.vk.lingvobot.application.levels.phrase.levelTwo;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.actions.MessageNew;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserPhrase;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.UserPhraseRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.PhraseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhraseLevelTwoEngRu implements IResponseMessageBodyHandler {

    private final UserRepository userRepository;
    private final PhraseService phraseService;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserPhraseRepository userPhraseRepository;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        if (messageBody.equals("BACK")) {
            user.setLevel(MenuLevel.MAIN.getCode());
            userRepository.save(user);
            actionLevel1(user.getVkId());
        } else {
            phraseService.actionLevel4(user.getVkId());
        }
    }

    private void actionLevel1(int userVkId) {
        List<String> levelFirst = new ArrayList<>();
        levelFirst.add("Phrases");
        levelFirst.add("Dialogs");
        levelFirst.add("Import dialog");

        User user = userRepository.findByVkId(userVkId);
        UserPhrase userPhrase = userPhraseRepository.findByUserId(user.getUserId());
        if (userPhrase != null) {
            userPhrase.setIsFinished(true);
            userPhraseRepository.save(userPhrase);
        }
        Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(levelFirst);
        messageService.sendMessageWithTextAndKeyboard(userVkId, "Дружище, ты в главном меню.", keyboardWithButtonsBrickByBrick);
    }
}
