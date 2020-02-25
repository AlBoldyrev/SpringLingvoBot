package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.DialogRepository;
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

    private final UserInfoServiceImpl userInfoService;
    private final DialogRepository dialogRepository;
    private final UserDialogServiceImpl userDialogService;
    private final MessageService messageService;
    private final SetupMessageService setupMessageService;
    private final PhrasePairStateService phrasePairStateService;
    private final PhrasePairService phrasePairService;
    private final UserService userService;
    private final GroupActor groupActor;
    private final MenuService menuService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        groupActor = this.groupActor;
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        String messageBody = message.getObject().getBody();

        User user = userInfoService.isExists(userVkId);
        if (user == null) {
            user = userService.createNewUser(userVkId);
        }
        boolean isInitialSetupNotCompleted = !setupMessageService.isInitialSetupCompleted(user);
        boolean hasUserDialogInProcess = userInfoService.hasUserDialogInProcess(user) && !isInitialSetupNotCompleted;
        boolean hasUserPhrasesDialogInProcess = phrasePairService.hasUserPhrasesDialogInProcess(user);

        if (isInitialSetupNotCompleted) {
            setupMessageService.processInitialSetup(user, groupActor, messageBody);
        }
        if (!isInitialSetupNotCompleted && !hasUserDialogInProcess && !hasUserPhrasesDialogInProcess) {
            menuService.handle(user, messageBody, groupActor);
        }
        if (hasUserPhrasesDialogInProcess) {
            userDialogService.processPhrasesPairDialog(user, groupActor, messageBody);
        }
        if (hasUserDialogInProcess) {
            userDialogService.processCommonDialog(user, groupActor);
        }
    }
}