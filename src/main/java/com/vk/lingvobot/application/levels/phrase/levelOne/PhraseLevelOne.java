package com.vk.lingvobot.application.levels.phrase.levelOne;

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
public class PhraseLevelOne implements IResponseMessageBodyHandler {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserPhraseRepository userPhraseRepository;
    private final PhraseService phraseService;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();
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
            actionLevel1(user.getVkId());

        } else if (messageBody.equals(Eng_Ru)) {
            user.setLevel(MenuLevel.ENG_RU.getCode());
            userRepository.save(user);
            phraseService.actionLevel4(user.getVkId());
        } else if (messageBody.equals(Ru_Eng)) {
            user.setLevel(MenuLevel.RU_ENG.getCode());
            userRepository.save(user);
            phraseService.actionLevel5(user.getVkId());
        } else {
            Keyboard keyboardWithIcons = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(icons);
            messageService.sendMessageWithTextAndKeyboard(user.getVkId(), "Выберите режим!", keyboardWithIcons);
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
