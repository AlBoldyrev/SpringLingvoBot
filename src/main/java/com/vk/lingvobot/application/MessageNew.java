package com.vk.lingvobot.application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.util.UserInfoService;
import com.vk.lingvobot.util.impl.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageNew implements IResponseHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoServiceImpl userInfoService;

    private Gson gson = new GsonBuilder().create();

    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int userId = message.getObject().getUserId();

        if (userInfoService.checkIfUserWroteTheMessageBefore(userId)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

        System.out.println("break");
    }
}
