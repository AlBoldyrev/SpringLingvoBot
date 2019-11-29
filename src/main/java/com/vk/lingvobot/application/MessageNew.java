package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.SetupKeyboard;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.*;
import com.vk.lingvobot.services.impl.UserDialogServiceImpl;
import com.vk.lingvobot.services.impl.UserInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageNew implements IResponseHandler {

    private final UserRepository userRepository;
    private final UserInfoServiceImpl userInfoService;
    private final UserDialogRepository userDialogRepository;
    private final DialogRepository dialogRepository;
    private final UserDialogServiceImpl userDialogService;
    private final MessageServiceKt messageService;
    private final SetupKeyboard setupKeyboard;
    private final SetupMessageService setupMessageService;
    private final SettingsRepository settingsRepository;
    private final UserService userService;
    private final DialogStateRepository dialogStateRepository;
    private final DialogMaxStateRepository dialogMaxStateRepository;
    private final GroupActor groupActor;
    private final MenuServiceKt menuServiceKt;
    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        groupActor = this.groupActor;

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        String messageBody = message.getObject().getBody();
        User user = userInfoService.isExists(userVkId);
        if (user == null) {
            user = createNewUser(userVkId);
        }

        if (!isInitialSetupCompleted(user)) {
            processInitialSetup(user, groupActor, messageBody);
        } else {
            if (!hasUserDialogInProcess(user)) {

                /*List<Dialog> allDialogs = dialogRepository.findAllDialogs();
                List<String> dialogsNames = allDialogs.stream().map(Dialog::getDialogName).collect(Collectors.toList());
                if (dialogsNames.contains(messageBody)) {
                    enterTheDialog(user, messageBody);
//                    sendListOfDialogs(user);
                } else if (messageBody.equals("Далее")) {
                    mainDialogServiceKt.callDialogListMenu(user, groupActor);
                } else {
                    mainDialogServiceKt.callMainMenu(user, messageBody, groupActor);
                }*/
            } else {
                processCommonDialog(user);
            }
        }

        if (message.getObject().getBody().equals("!меню")) {
            System.out.println("ВЫЗЫВАЕТСЯ МЕНЮ!");
            List<Dialog> dialogs = dialogRepository.findAllDialogs();
            List<String> dialogsNames = dialogs.stream().map(Dialog::getDialogName).collect(Collectors.toList());
            dialogsNames.forEach(System.out::println);
        }
    }


    /**
     * Checking if user finished initial setup and creating new setup UserDialog with dialog_id = 1 in database for new users. "Greeting dialog"
     */
    private void processInitialSetup(User user, GroupActor groupActor, String messageBody) {
        UserDialog greetingSetUpDialog = userInfoService.checkGreetingSetupDialog(user);

        if (greetingSetUpDialog == null) {
            setupMessageService.handle(user, groupActor, messageBody);
        } else {
            log.info("Initial setup for user: " + user.getUserName() + " is already finished.");
        }
    }


    /**
     * User sends us name of the particular dialog via Keyboard and we create UserDialog object using this data
     */
    private void enterTheDialog(User user, String message) {
        Dialog dialog = dialogRepository.findByDialogName(message);
        if (dialog == null) {
            log.error("dialog with unexisting name");
        } else {
            UserDialog userDialog = new UserDialog(user, dialog, false, false);
            userDialog.setState(1);
            userDialogService.create(userDialog);
        }
    }


    /**
     * When user ends setup dialog and send us message to see the list of all dialogs. Using Kotlin here for effective Keyboard usage.
     */
    private void sendListOfDialogs(User user) {
        List<Dialog> allDialogs = dialogRepository.findAllDialogExceptSettingOne();
        List<String> dialogsNames = allDialogs.stream().map(Dialog::getDialogName).collect(Collectors.toList());
//        StringBuilder sb = new StringBuilder();
//        dialogsNames.forEach(sb::append);
        menuServiceKt.processMainDialog(user, groupActor, dialogsNames);
        /*messageService.sendMessageTextOnly(groupActor, user.getUserVkId(), sb.toString());*/
    }


    /**
     * Checks if User has any UserDialog which is not cancelled or not finished
     */
    private boolean hasUserDialogInProcess(User user) {
        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(user.getUserId());
        return currentDialogOfUser != null;
    }

    private boolean isInitialSetupCompleted(User user) {
        UserDialog greetingSetUpDialog = userInfoService.findUserGreetingDialog(user);
        if (greetingSetUpDialog == null) {
            return false;
        }
        return greetingSetUpDialog.isFinished() || greetingSetUpDialog.isCancelled();
    }


    /**
     * User sends the message - we send particular dialogPhrase and increment state
     */
    private void processCommonDialog(User user) {

        UserDialog currentUserDialog = userDialogService.findCurrentDialogOfUser(user.getUserId());
        Integer state = currentUserDialog.getState();
        DialogState dialogState = dialogStateRepository.findByDialogIdAndState(currentUserDialog.getDialog().getDialogId(), state);
        DialogPhrase dialogPhrase = dialogState.getDialogPhrase();
        String dialogPhraseValue = dialogPhrase.getDialogPhraseValue();

        messageService.sendMessageTextOnly(groupActor, user.getVkId(), dialogPhraseValue);

        DialogMaxState dialogMaxState = dialogMaxStateRepository.findByDialogId(currentUserDialog.getDialog().getDialogId());
        Integer dialogMaxStateValue = dialogMaxState.getDialogMaxStateValue();

        if (++state <= dialogMaxStateValue) {
            currentUserDialog.setState(state);
        } else {
            currentUserDialog.setFinished(true);
        }
        userDialogRepository.save(currentUserDialog);

    }

    /**
     * Create new user using his vkId.
     */
    private User createNewUser(int vkId) {
        log.info("There is no user with vk id: " + vkId + ". Creating new user...");

        User user = new User(vkId);

        Settings settings = new Settings();
        Settings saveSettings = settingsRepository.save(settings);
        user.setSettings(saveSettings);
        return userRepository.save(user);
    }
}
