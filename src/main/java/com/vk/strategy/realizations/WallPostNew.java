package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.strategy.Executor;

public class WallPostNew implements Executor {

    public void execute(JsonObject jsonObject, VkApiClient apiClient, GroupActor actor) {
        System.out.println("WALL POST NEW!!!!");
    }
}
