package com.vk.lingvobot.application;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;

public interface IResponseHandler {

    void handle(JsonObject jsonObject, GroupActor groupActor);

}
