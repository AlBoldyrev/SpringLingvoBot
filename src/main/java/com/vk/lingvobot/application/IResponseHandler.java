package com.vk.lingvobot.application;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;

public interface IResponseHandler {

    void handle(JsonObject jsonObject, GroupActor groupActor) throws ClientException;

}
