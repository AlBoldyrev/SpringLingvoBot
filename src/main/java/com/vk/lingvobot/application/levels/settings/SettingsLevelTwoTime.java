package com.vk.lingvobot.application.levels.settings;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.enumeration.PeriodOfTime;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.SettingsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;

import static io.vavr.API.*;
import static io.vavr.Predicates.is;

@Component
@RequiredArgsConstructor
public class SettingsLevelTwoTime extends SettingsLevelOne implements IResponseMessageBodyHandler {

    private static final Pattern EXACT_TIME_PATTERN = Pattern.compile("^(?:[01]\\d|2[0123]):(?:[012345]\\d)$");
    private static final Pattern TIME_RANGE_PATTERN =
            Pattern.compile("^(?:[01]\\d|2[0123]):(?:[012345]\\d)-(?:[01]\\d|2[0123]):(?:[012345]\\d)$");


    private final MessageService messageService;
    private final SettingsService settingsService;
    private final CustomJavaKeyboard customJavaKeyboard;

    private final List<String> partOfTheDayButtonNames = io.vavr.collection.List.of(
            PeriodOfTime.MORNING.getName(),
            PeriodOfTime.AFTERNOON.getName(),
            PeriodOfTime.EVENING.getName(),
            PeriodOfTime.NIGHT.getName(),
            CANCEL)
            .asJava();

    private final List<String> cancelButtonName = io.vavr.collection.List.of(CANCEL).asJava();


    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        Match(messageBody).of(
                Case($(is(EXACT_TIME)), () -> run(() -> showExactTimeTip(user))),

                Case($(is(TIME_RANGE)), () -> run(() -> showTimeRangeTip(user))),

                Case($(is(PART_OF_THE_DAY)), () -> run(() -> showPartOfTheDayTip(user))),

                Case($(is(PeriodOfTime.MORNING.getName())), () -> run(() -> setPartOfTheDay(user, PeriodOfTime.MORNING))),

                Case($(is(PeriodOfTime.AFTERNOON.getName())), () -> run(() -> setPartOfTheDay(user, PeriodOfTime.AFTERNOON))),

                Case($(is(PeriodOfTime.EVENING.getName())), () -> run(() -> setPartOfTheDay(user, PeriodOfTime.EVENING))),

                Case($(is(PeriodOfTime.NIGHT.getName())), () -> run(() -> setPartOfTheDay(user, PeriodOfTime.NIGHT))),

                Case($(is(BACK)), () -> run(() -> returnToSettingsOneMenu(user))),

                Case($(is(CANCEL)), () -> run(() -> showTimeSettings(user))),

                Case($(), () -> run(() -> setTimeOrShowTimeMenu(user, messageBody)))
        );

    }

    private void setTimeOrShowTimeMenu(User user, String message) {
        if (EXACT_TIME_PATTERN.matcher(message).matches()) {
            LocalTime parsedExactTime = LocalTime.parse(message);
            settingsService.setExactLessonTime(user, parsedExactTime);
            showTimeSettings(user);
        } else if (TIME_RANGE_PATTERN.matcher(message).matches()) {
            String[] split = StringUtils.split(message, '-');
            if (split.length == 2) {
                saveTimeRange(user, split);
            } else {
                showTimeRangeTip(user);
            }
        } else {
            showTimeSettings(user);
        }
    }

    private void showExactTimeTip(User user) {
        Keyboard keyboard = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(cancelButtonName);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Введите точное время занятий. Формат времени: ЧЧ:ММ", keyboard);
    }

    private void showTimeRangeTip(User user) {
        Keyboard keyboard = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(cancelButtonName);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Введите в какой период времени проводить занятия. Формат времени: ЧЧ:ММ-ЧЧ:ММ", keyboard);
    }

    private void showPartOfTheDayTip(User user) {
        Keyboard partOfTheDayKeyboard =
                customJavaKeyboard.createKeyboardWithButtonsNButtonsPerRow(partOfTheDayButtonNames, 2);
        messageService.sendMessageWithTextAndKeyboard(user.getVkId(),
                "Введите когда вы ходите заниматься",
                partOfTheDayKeyboard);
    }

    private void setPartOfTheDay(User user, PeriodOfTime period) {
        settingsService.setPartOfTheDay(user, period);
        showTimeSettings(user);
    }

    private void returnToSettingsOneMenu(User user) {
        setTheLevel(user, MenuLevel.SETTINGS.getCode());
        showSettingsMenu(user);
    }

    private void saveTimeRange(User user, String[] split) {
        LocalTime start = LocalTime.parse(split[0]);
        LocalTime end = LocalTime.parse(split[1]);
        if (checkTimeRange(start, end)) {
            settingsService.setLessonTimeRange(user, start, end);
            showTimeSettings(user);
        } else {
            messageService.sendMessageTextOnly(user.getVkId(),
                    "Временной диапазон не должен быть задом наперёд");
            showTimeRangeTip(user);
        }
    }

    private boolean checkTimeRange(LocalTime start, LocalTime end) {
        return Match(start.compareTo(end)).of(
                Case($(is(-1)), true),
                Case($(is(0)), true),
                Case($(is(1)), false)
        );
    }

}
