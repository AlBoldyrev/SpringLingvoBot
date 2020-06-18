package com.vk.lingvobot.application.levels.settings;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.application.levels.Menu;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.enumeration.DifficultyLevel;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

@Component
public class SettingsLevelOne extends Menu implements IResponseMessageBodyHandler {

    private static final String DIFFICULTY = "Difficulty";
    private static final String TIME_SETTINGS = "Time settings";
    private static final String LESSONS_FREQUENCY = "Lessons' frequency";
    private static final String ONE_LESSON = "1";
    private static final String TWO_LESSONS = "2";
    private static final String THREE_LESSONS = "3";
    private static final String FOUR_LESSONS = "4";
    private static final String FIVE_LESSONS = "5";

    static final String EXACT_TIME = "Lesson exact time";
    static final String TIME_RANGE = "Lesson in time period";
    static final String PART_OF_THE_DAY = "Lesson's part of the day";
    final static String CANCEL = "Cancel";


    @Autowired
    private MessageService messageService;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private CustomJavaKeyboard customJavaKeyboard;

    private final List<String> settingsLevelButtonNames = io.vavr.collection.List.of(
            DIFFICULTY,
            TIME_SETTINGS,
            LESSONS_FREQUENCY,
            BACK)
            .asJava();
    private final List<String> lessonFrequencyButtonNames = io.vavr.collection.List.of(
            ONE_LESSON,
            TWO_LESSONS,
            THREE_LESSONS,
            FOUR_LESSONS,
            FIVE_LESSONS,
            CANCEL)
            .asJava();
    private final List<String> difficultySettingsButtonNames = io.vavr.collection.List.of(
            DifficultyLevel.BEGINNER.getName(),
            DifficultyLevel.ELEMENTARY.getName(),
            DifficultyLevel.INTERMEDIATE.getName(),
            DifficultyLevel.UPPER_INTERMEDIATE.getName(),
            DifficultyLevel.ADVANCED.getName(),
            DifficultyLevel.PROFICIENCY.getName(),
            BACK)
            .asJava();
    private final List<String> timeSettingsButtonNames = io.vavr.collection.List.of(
            EXACT_TIME,
            TIME_RANGE,
            PART_OF_THE_DAY,
            BACK)
            .asJava();

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        Match(messageBody).of(
                Case($(is(BACK)), () -> run(() -> returnToMainMenu(user))),
                Case($(is(CANCEL)), () -> run(() -> showSettingsMenu(user))),
                Case($(is(DIFFICULTY)), () -> run(() -> switchToDifficultyLevel(user))),
                Case($(is(TIME_SETTINGS)), () -> run(() -> switchToTimeLevel(user))),
                Case($(is(LESSONS_FREQUENCY)), () -> run(() -> showLessonsFrequencyTip(user))),
                Case($(is(ONE_LESSON)), () -> run(() -> setLessonsFrequency(user, messageBody))),
                Case($(is(TWO_LESSONS)), () -> run(() -> setLessonsFrequency(user, messageBody))),
                Case($(is(THREE_LESSONS)), () -> run(() -> setLessonsFrequency(user, messageBody))),
                Case($(is(FOUR_LESSONS)), () -> run(() -> setLessonsFrequency(user, messageBody))),
                Case($(is(FIVE_LESSONS)), () -> run(() -> setLessonsFrequency(user, messageBody))),
                Case($(), () -> run(() -> showSettingsMenu(user)))
        );

    }

    void showSettingsMenu(User user) {
        Keyboard settingsKeyboard =
                customJavaKeyboard.createKeyboardWithButtonsNButtonsPerRow(settingsLevelButtonNames, 2);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Вы в меню настроек",
                settingsKeyboard);
    }

    void showDifficultySettings(User user) {
        Keyboard keyboard = customJavaKeyboard.createKeyboardWithButtonsNButtonsPerRow(difficultySettingsButtonNames, 2);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Выберите уровень сложности:",
                keyboard);
    }

    void showTimeSettings(User user) {
        Keyboard generalTimeSettingsKeyboard =
                customJavaKeyboard.createKeyboardWithButtonsNButtonsPerRow(timeSettingsButtonNames, 2);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Выберите время занятий:",
                generalTimeSettingsKeyboard);
    }

    private void showLessonsFrequencyTip(User user) {
        Keyboard lessonsFreqKeyboard =
                customJavaKeyboard.createKeyboardWithButtonsNButtonsPerRow(lessonFrequencyButtonNames, 2);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Выберите частоту занятий: ",
                lessonsFreqKeyboard);
    }

    private void setLessonsFrequency(User user, String message) {
        int lessonsFrequency = Integer.parseInt(message);
        settingsService.setBotWriteFrequency(user, lessonsFrequency);
        showLessonsFrequencyTip(user);
    }

    private void switchToDifficultyLevel(User user) {
        setTheLevel(user, MenuLevel.DIFFICULTY_SETTINGS.getCode());
        showDifficultySettings(user);
    }

    private void switchToTimeLevel(User user) {
        setTheLevel(user, MenuLevel.TIME_SETTINGS.getCode());
        showTimeSettings(user);
    }

    private void returnToMainMenu(User user) {
        setTheLevel(user, MenuLevel.MAIN.getCode());
        baseMenuAction(user);
    }
}
