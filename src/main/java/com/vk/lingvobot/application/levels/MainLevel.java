package com.vk.lingvobot.application.levels;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.levels.dialog.DialogLevelOne;
import com.vk.lingvobot.application.levels.phrase.levelOne.PhraseLevelOne;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.UserPhraseRepository;
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
public class MainLevel extends Menu implements IResponseMessageBodyHandler {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final DialogLevelOne dialogLevelOne;
    private final PhraseLevelOne phraseLevelOne;
    private final UserPhraseRepository userPhraseRepository;
    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();
        switch (messageBody) {
            case "Phrases":
                setTheLevel(user, MenuLevel.PHRASE.getCode());
                phraseLevelOne.handle(user, message);
                break;
            case "Dialogs":
                setTheLevel(user, MenuLevel.DIALOGS.getCode());
                dialogLevelOne.handle(user, message);
                break;
            case "Import dialog":
                List<String> back = new ArrayList<>();
                back.add("BACK");
                Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(back);
                messageService.sendMessageWithTextAndKeyboard(user.getVkId(), "Пожалуйста, отправь мне txt файл с новыми диалогом.", keyboardWithButtonsBrickByBrick);
                setTheLevel(user, MenuLevel.IMPORT_DIALOG.getCode());
                break;
            default:
                baseMenuAction(user);
                break;
        }
    }



}
