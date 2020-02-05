package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.factory.AbstractFactory;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.SetupMessageService;
import com.vk.lingvobot.services.UserInfoService;
import com.vk.lingvobot.states.SettingsSetupState;
import com.vk.lingvobot.util.Dialogs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
class SetupMessageServiceImpl implements SetupMessageService {

    private final DialogRepository dialogRepository;
    private final DialogMaxStateRepository dialogMaxStateRepository;
    private final DialogStateRepository dialogStateRepository;
    private final UserDialogRepository userDialogRepository;
    private final SettingsRepository settingsRepository;
    private final AbstractFactory<SettingsSetupState> stateFactory;
    private final UserInfoService userInfoService;


    private SettingsSetupState settingsSetupState;

    private Map<String, Integer> difficultyLevel = Stream.of(new Object[][]{
            {"Лёгкий", 1},
            {"Средний", 1},
            {"Сложный", 1},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));


    @Override
    public void handle(User user, GroupActor groupActor, String messageBody) {
        UserDialog greetingSetUpDialog = userDialogRepository.findUserGreetingDialog(user.getUserId());

        Dialog setupDialog = dialogRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.getValue());
        DialogMaxState setupDialogMaxState = dialogMaxStateRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.getValue());

        if (greetingSetUpDialog == null) {
            greetingSetUpDialog = new UserDialog(user, setupDialog, false, false, false);
            greetingSetUpDialog.setState(1);
            userDialogRepository.save(greetingSetUpDialog);
        }

        Settings userSettings = settingsRepository.findBySettingsId(user.getSettings().getSettingsId());
        if (userSettings == null) {
            userSettings = new Settings();
            user.setSettings(userSettings);
        }

        DialogState dialogToState = dialogStateRepository.findByDialogIdAndState(Dialogs.GREETING_SET_UP_DIALOG.getValue(),
                greetingSetUpDialog.getState());

        if (dialogToState.getState() <= setupDialogMaxState.getDialogMaxStateValue()) {
            settingsSetupState = stateFactory.getInstance(dialogToState.getState());
            settingsSetupState.handle(user, groupActor, dialogToState, greetingSetUpDialog);
            processSettings(userSettings, messageBody);
        }
    }

    //Checks user answers. One of the first candidates for refactoring
    private void processSettings(Settings userSettings, String messageBody) {
        switch (messageBody) {
            case "Ты":
            case "Вы":
                userSettings.setPronoun(messageBody);
                settingsRepository.save(userSettings);
                break;
            case "Лёгкий":
            case "Средний":
            case "Сложный":
                userSettings.setDifficultyLevel(difficultyLevel.get(messageBody));
                settingsRepository.save(userSettings);
                break;
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
                userSettings.setLessonsPerDay(Integer.parseInt(messageBody));
                settingsRepository.save(userSettings);
                break;
            case "Утром":
            case "Днём":
            case "Вечером":
                userSettings.setPartOfTheDay(messageBody);
                settingsRepository.save(userSettings);
                break;
        }
    }

    /**
     * Checking if user finished initial setup and creating new setup UserDialog with dialog_id = 1 in database for new users. "Greeting dialog"
     */
    public void processInitialSetup(User user, GroupActor groupActor, String messageBody) {
        UserDialog greetingSetUpDialog = userInfoService.checkGreetingSetupDialog(user);

        if (greetingSetUpDialog == null) {
            handle(user, groupActor, messageBody);
        } else {
            log.info("Initial setup for user: " + user.getUserName() + " is already finished.");
        }
    }




    public boolean isInitialSetupCompleted(User user) {
        UserDialog greetingSetUpDialog = userInfoService.findUserGreetingDialog(user);
        if (greetingSetUpDialog == null) {
            return false;
        }
        return greetingSetUpDialog.getIsFinished() || greetingSetUpDialog.getIsCancelled();
    }
}