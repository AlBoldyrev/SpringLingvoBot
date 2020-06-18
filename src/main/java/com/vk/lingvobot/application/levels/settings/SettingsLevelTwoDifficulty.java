package com.vk.lingvobot.application.levels.settings;

import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.enumeration.DifficultyLevel;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

@Component
@RequiredArgsConstructor
public class SettingsLevelTwoDifficulty extends SettingsLevelOne implements IResponseMessageBodyHandler {

    private final MessageService messageService;
    private final SettingsService settingsService;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        Match(messageBody).of(
                Case($(is(DifficultyLevel.BEGINNER.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.BEGINNER))),

                Case($(is(DifficultyLevel.ELEMENTARY.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.ELEMENTARY))),

                Case($(is(DifficultyLevel.INTERMEDIATE.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.INTERMEDIATE))),

                Case($(is(DifficultyLevel.UPPER_INTERMEDIATE.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.UPPER_INTERMEDIATE))),

                Case($(is(DifficultyLevel.ADVANCED.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.ADVANCED))),

                Case($(is(DifficultyLevel.PROFICIENCY.getName())),
                        () -> run(() -> setDifficulty(user, DifficultyLevel.PROFICIENCY))),

                Case($(is(BACK)), () -> run(() -> returnToSettingsOneMenu(user))),

                Case($(), () -> run(() -> showDifficultySettings(user)))
        );
    }

    private void setDifficulty(User user, DifficultyLevel difficultyLevel) {
        String answer = String.format("Установлен %s уровень сложности.", difficultyLevel.getName());
        settingsService.setDifficultyLevel(user, difficultyLevel);
        messageService.sendMessageTextOnly(user.getVkId(), answer);
        showDifficultySettings(user);
    }

    private void returnToSettingsOneMenu(User user) {
        setTheLevel(user, MenuLevel.SETTINGS.getCode());
        showSettingsMenu(user);
    }

}
