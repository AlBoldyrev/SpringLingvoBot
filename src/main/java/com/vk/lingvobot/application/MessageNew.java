package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.SetupKeyboard;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.UserService;
import com.vk.lingvobot.services.impl.UserDialogServiceImpl;
import com.vk.lingvobot.services.impl.UserInfoServiceImpl;
import com.vk.lingvobot.util.Dialogs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MessageNew implements IResponseHandler {

    private final UserRepository userRepository;

    private final UserInfoServiceImpl userInfoService;

    private final UserDialogRepository userDialogRepository;

    private final DialogRepository dialogRepository;

    private final UserDialogServiceImpl userDialogService;

    private final MessageServiceKt messageService;

    private final SetupKeyboard setupKeyboard;

    private final UserService userService;

    private final DialogStateRepository dialogStateRepository;

    private final DialogMaxStateRepository dialogMaxStateRepository;


    private Gson gson = new GsonBuilder().create();

    @Autowired
    public MessageNew(UserRepository userRepository, UserInfoServiceImpl userInfoService,
                      UserDialogRepository userDialogRepository, DialogRepository dialogRepository,
                      UserDialogServiceImpl userDialogService, SetupKeyboard setupKeyboard,
                      MessageServiceKt messageService, UserService userService, DialogStateRepository dialogStateRepository,
                      DialogMaxStateRepository dialogMaxStateRepository) {
        this.userRepository = userRepository;
        this.userInfoService = userInfoService;
        this.userDialogRepository = userDialogRepository;
        this.dialogRepository = dialogRepository;
        this.userDialogService = userDialogService;
        this.setupKeyboard = setupKeyboard;
        this.messageService = messageService;
        this.userService = userService;
        this.dialogStateRepository = dialogStateRepository;
        this.dialogMaxStateRepository = dialogMaxStateRepository;
    }

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        User user = userInfoService.isExists(userVkId);

        if (user == null) {
            user = createNewUser(userVkId);
        }

        checkInitialSetup(user, groupActor);
    //-----------

        UserDialog currentUserDialog = userDialogService.findCurrentDialogOfUser(user.getUserId());
        Integer state = currentUserDialog.getState();
        DialogState dialogState = dialogStateRepository.findByDialogIdAndState(currentUserDialog.getDialog().getDialogId(), state);
        DialogPhrase dialogPhrase = dialogState.getDialogPhrase();
        String dialogPhraseValue = dialogPhrase.getDialogPhraseValue();

        messageService.sendMessageTextOnly(groupActor, user.getUserVkId(), dialogPhraseValue);
        log.info("Сообщение отправлено! ");

        DialogMaxState dialogMaxState = dialogMaxStateRepository.findByDialogId(currentUserDialog.getDialog().getDialogId());
        Integer dialogMaxStateValue = dialogMaxState.getDialogMaxStateValue();

        log.info("CURRENT STATE : " + state);
        log.info("DIALOG MAX STATE : " + dialogMaxStateValue);
        if (++state <= dialogMaxStateValue) {
            currentUserDialog.setState(state);
            log.info("CURRENT STATE AFTER ++ : " + state);

        } else {
            currentUserDialog.setFinished(true);
        }
        userDialogRepository.save(currentUserDialog);


        //-----------
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
    private void checkInitialSetup(User user, GroupActor groupActor) {


        /*UserDialog userDialog = new UserDialog();
        userDialog.setUser(user);
        userDialogService.create(userDialog);*/
       /* UserDialog setupUserDialog = userInfoService.getGreetingSetupDialog(user);

        if (setupUserDialog != null && setupUserDialog.isFinished()) {
            log.info("Initial setup for user: " + user.getUserName() + " is already finished.");
            messageService.sendMessageTextOnly(groupActor, user.getUserId(), " setup finished! ");
            return;
        }
        if (setupUserDialog == null) {
            Dialog setupDialog2 = dialogRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.getValue());
            setupUserDialog = new UserDialog(user, setupDialog2, false, false);

            userDialogService.create(setupUserDialog);
            log.info("Initial setup for user: " + user.getUserName() + " has just begun!");
            messageService.sendMessageTextOnly(groupActor, user.getUserId(), " setup NOT finished! ");
        }*/

/*        List<String> labels = Arrays.asList("На Вы!", "На Ты!");
        messageService.sendMessageWithTextAndKeyboard(groupActor, user.getUserVkId(), "Как к тебе обращаться?", labels);*/
        
//        Dialog startingDialog = dialogRepository.findStartingDialog();
//        UserDialog userDialog = new UserDialog(user, startingDialog, false, false);
//        messageService.sendMessageWithTextAndKeyboard(userVkId, startingDialog.getDialogPhrase().getDialogPhraseValue(), Dialog1.KEYBOARD1);
//        userDialogRepository.save(userDialog);
    }

    private User createNewUser(int vkId) {
        log.info("There is no user with vk id: " + vkId + ". Creating new user...");

        User user = new User(vkId);
        User saved = userRepository.save(user);

        if (saved != null) {
            log.info("New user created: " + user);
            return saved;
        }

        return null;
    }


    /*private int findCurrentDialogOfUser(int userVkId) {

        UserDialog currentDialogOfUser = userDialogService.findCurrentDialogOfUser(userVkId);
        if (currentDialogOfUser == null) {
            log.error("There is no active dialog for this user!!!");
            return 0; //TODO magicNumber!
        }
        return currentDialogOfUser.getDialog().getDialogId();
    }*/



}
