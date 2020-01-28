package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.User;

public interface SetupMessageService {

    void handle(User user, GroupActor groupActor, String messageBody);
    void processInitialSetup(User user, GroupActor groupActor, String messageBody);
    boolean isInitialSetupCompleted(User user);
}
