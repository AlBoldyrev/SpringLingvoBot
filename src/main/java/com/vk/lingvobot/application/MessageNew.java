package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.util.impl.UserDialogServiceImpl;
import com.vk.lingvobot.util.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageNew implements IResponseHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private UserDialogRepository userDialogRepository;

    @Autowired
    private DialogRepository dialogRepository;

    @Autowired
    private UserDialogServiceImpl userDialogServiceImpl;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int userVkId = message.getObject().getUserId();
        int currentDialogOfUser = findCurrentDialogOfUser(userVkId);

        log.info("Current dialog of user " + userVkId + " is: " + currentDialogOfUser);


        if (userInfoService.checkIfUserWroteTheMessageBefore(userVkId)) {
            log.info("We know this user with id: " + userVkId);
        } else {
            log.info("This user with id : " + userVkId + " has never written us before!");
            initUserInLingvoBot(userVkId);
        }
    }

    /**
     * Creating new user and new UserDialog via {@param userVkId} with dialog_id = 1 in database. "Greeting dialog"
     */
    public void initUserInLingvoBot(int userVkId) {

        User user = new User(userVkId);
        userRepository.save(user);

        Dialog startingDialog = dialogRepository.findStartingDialog();
        UserDialog userDialog = new UserDialog(user, startingDialog, false, false);
        userDialogRepository.save(userDialog);

    }


    private int findCurrentDialogOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogServiceImpl.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 0; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getDialogPK().getDialogId();
    }
}
