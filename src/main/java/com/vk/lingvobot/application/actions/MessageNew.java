package com.vk.lingvobot.application.actions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.application.levels.dialog.DialogLevelOne;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.application.levels.MainLevel;
import com.vk.lingvobot.application.levels.phrase.levelOne.PhraseLevelOne;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoEngRu;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoRuEng;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.*;
import com.vk.lingvobot.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageNew implements IResponseHandler {

    private final GroupActor groupActor;

    private final UserInfoService userInfoService;
    private final SettingsService settingsService;

    private final MainLevel mainLevel;
    private final DialogLevelOne dialogLevelOne;
    private final PhraseLevelOne phraseLevelOne;
    private final PhraseLevelTwoRuEng phraseLevelTwoRuEng;
    private final PhraseLevelTwoEngRu phraseLevelTwoEngRu;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);

        int userVkId = message.getObject().getUserId();
        String messageBody = message.getObject().getBody();
        User user = userInfoService.isExists(userVkId);

        if (user == null) { 
            user = settingsService.createNewUser(userVkId);
        }

        Integer userLevel = user.getLevel();

        Map<Integer, IResponseMessageBodyHandler> strategyHandlers = new HashMap<>();
        strategyHandlers.put(MenuLevel.MAIN.getCode(), mainLevel);
        strategyHandlers.put(MenuLevel.DIALOGS.getCode(), dialogLevelOne);
        strategyHandlers.put(MenuLevel.PHRASE.getCode(), phraseLevelOne);
        strategyHandlers.put(MenuLevel.ENG_RU.getCode(), phraseLevelTwoEngRu);
        strategyHandlers.put(MenuLevel.RU_ENG.getCode(), phraseLevelTwoRuEng);

        IResponseMessageBodyHandler responseHandler = strategyHandlers.get(userLevel);

        responseHandler.handle(user, messageBody);

    }






}