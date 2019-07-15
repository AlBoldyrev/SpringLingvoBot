package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.Dialog1;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.impl.MessageServiceImpl;
import com.vk.lingvobot.services.impl.UserDialogServiceImpl;
import com.vk.lingvobot.services.impl.UserInfoServiceImpl;
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
    private UserDialogServiceImpl userDialogService;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private DialogPhraseRepository dialogPhraseRepository;

    @Autowired
    private Dialog1 dialog1;

    @Autowired
    private DialogMaxStateRepository dialogMaxStateRepository;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        int currentDialogIdOfUser = findCurrentDialogOfUser(userVkId);
        Integer maxStateValue = 0;
        if (currentDialogIdOfUser != 0) {
            maxStateValue = dialogMaxStateRepository.findByDialogId(currentDialogIdOfUser).getMaxStateValue();
            log.info("Seems it's starting dialog");
        }
        int currentStateOfUser = findCurrentStateOfUser(userVkId);



        log.info("Current dialog of user " + userVkId + " is: " + currentDialogIdOfUser);

        if (userInfoService.checkIfUserWroteTheMessageBefore(userVkId)) {

            UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
            Integer state = currentDialogOfUser.getDialog().getState();

            log.info("We know this user with id: " + userVkId);
            log.info("Current state is : " + state);
            String dialogPhraseValue = dialogRepository.findDialogByDialogId(currentDialogIdOfUser, state).getDialogPhrase().getDialogPhraseValue();
            log.info("Phrase is : " + dialogPhraseValue);

            messageService.sendMessageWithTextAndKeyboard(userVkId, dialogPhraseValue, dialog1.getStateAndKeyboard().get(2));

            currentDialogOfUser.getDialog().setState(++state);
            if (state.equals(maxStateValue)) {
                currentDialogOfUser.setFinished(true);
            }
            userDialogRepository.save(currentDialogOfUser);
            log.info("State of dialog with id: " + currentDialogIdOfUser + " is changed from " + --state + " to " + ++state);



        } else {
            log.info("This user with id : " + userVkId + " has never written us before!");
            initUserInLingvoBot(userVkId);
        }
    }

    /**
     * Creating new user and new UserDialog via {@param userVkId} with dialog_id = 1 in database. "Greeting dialog"
     */
    private void initUserInLingvoBot(int userVkId) {

        User user = new User(userVkId);
        userRepository.save(user);

        Dialog startingDialog = dialogRepository.findStartingDialog();
        UserDialog userDialog = new UserDialog(user, startingDialog, false, false);
        messageService.sendMessageWithTextAndKeyboard(userVkId, startingDialog.getDialogPhrase().getDialogPhraseValue(), Dialog1.KEYBOARD1);
        userDialogRepository.save(userDialog);
    }


    private int findCurrentDialogOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 0; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getDialogId();
    }

    private int findCurrentStateOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 1; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getState();
    }
}
