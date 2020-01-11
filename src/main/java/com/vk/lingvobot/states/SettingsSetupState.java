package com.vk.lingvobot.states;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;

public interface SettingsSetupState {
    void handle(User user, GroupActor groupActor, DialogState dialogState, UserDialog greetingSetUpDialog);
}
