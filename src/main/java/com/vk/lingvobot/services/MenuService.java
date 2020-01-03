package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.User;

import java.util.List;

public interface MenuService {
    void handle(User user, String messageBody, GroupActor groupActor);
    void processMainDialog(User user, GroupActor groupActor, List<String> buttonList);
}
