package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.keyboards.SetupKeyboard;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.SetupMessageService;
import com.vk.lingvobot.services.impl.UserDialogServiceImpl;
import com.vk.lingvobot.services.impl.UserInfoServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        String messageBody = message.getObject().getBody();
        User user = userInfoService.isExists(userVkId);
        if (user == null) {
            user = createNewUser(userVkId);
        }

        checkInitialSetup(user, groupActor, messageBody);

    }

    /**
     * Checking if user finished initial setup and creating new setup UserDialog with dialog_id = 1 in database for new users. "Greeting dialog"
     */
    private void checkInitialSetup(User user, GroupActor groupActor, String messageBody) {
        UserDialog greetingSetUpDialog = userInfoService.checkGreetingSetupDialog(user);

        if (greetingSetUpDialog == null) {
            setupMessageService.handle(user, groupActor, messageBody);
        } else {
            log.info("Initial setup for user: " + user.getUserName() + " is already finished.");
        }
    }

    private User createNewUser(int vkId) {
        log.info("There is no user with vk id: " + vkId + ". Creating new user...");

        User user = new User(vkId);

        Settings settings = new Settings();
        Settings saveSettings = settingsRepository.save(settings);
        user.setSettings(saveSettings);
        User saved = userRepository.save(user);

        if (saved != null) {
            log.info("New user created: " + user);
            return saved;
        }

        return null;
    }



    private int findCurrentDialogOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 0; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getDialogId();
    }

    /*private int findCurrentStateOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 1; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getState();
    }*/

    /*public void createMasterDataForTestingDialogs() {

        Dialog dialog = new Dialog();
        DialogMaxState dialogMaxState = new DialogMaxState();
        dialogMaxState.setDialog(dialog);
        dialogMaxState.setDialogMaxStateValue(5);
        dialog.setDialogId(1);
        dialogMa
        dialogRepository.save(dialog);
        System.out.println("dialog has been saved...");
    }*/
}
