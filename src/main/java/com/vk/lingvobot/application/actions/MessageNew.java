package com.vk.lingvobot.application.actions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.application.levels.dialog.DialogLevelOne;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.application.levels.MainLevel;
import com.vk.lingvobot.application.levels.importDialog.ImportDialogLevel;
import com.vk.lingvobot.application.levels.phrase.levelOne.PhraseLevelOne;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoEngRu;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoRuEng;
import com.vk.lingvobot.application.levels.settings.SettingsLevelOne;
import com.vk.lingvobot.application.levels.settings.SettingsLevelTwoDifficulty;
import com.vk.lingvobot.application.levels.settings.SettingsLevelTwoTime;
import com.vk.lingvobot.application.strategy.StrategyHandlerService;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageNew implements IResponseHandler {

    private final GroupActor groupActor;

    private final UserInfoService userInfoService;
    private final SettingsService settingsService;
    private final StrategyHandlerService strategyHandlerService;

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

        IResponseMessageBodyHandler responseHandler = strategyHandlerService.getStrategyHandlers().get(userLevel);

        responseHandler.handle(user, message);

    }

}