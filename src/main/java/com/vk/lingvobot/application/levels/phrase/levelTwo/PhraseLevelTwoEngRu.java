package com.vk.lingvobot.application.levels.phrase.levelTwo;

import com.vk.lingvobot.application.levels.Menu;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
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

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhraseLevelTwoEngRu extends Menu implements IResponseMessageBodyHandler {

    private final UserRepository userRepository;
    private final PhraseService phraseService;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserPhraseRepository userPhraseRepository;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        if (messageBody.equals("BACK")) {
            setTheLevel(user, MenuLevel.MAIN.getCode());
            baseMenuAction(user);
        } else {
            phraseService.actionForRuEngTranslation(user);
        }
    }
}
