package com.vk.application;


import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

public class MessageNew implements IResponseHandler {
    @Override
    public void handle(JsonObject jsonObject, GroupActor groupActor) {

    }
}
