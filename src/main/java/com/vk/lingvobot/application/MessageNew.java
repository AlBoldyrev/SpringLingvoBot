package com.vk.lingvobot.application;


import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageNew implements IResponseHandler {
    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

        System.out.println("lol");
    }
}
