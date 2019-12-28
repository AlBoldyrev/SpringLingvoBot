package com.vk.lingvobot.states.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.states.SettingsSetupState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EndSetup implements SettingsSetupState {

    private final MessageServiceKt messageService;
    private final UserDialogRepository userDialogRepository;

    @Override
    public void handle(User user, GroupActor groupActor, DialogState dialogState, UserDialog greetingSetUpDialog) {
        messageService.sendMessageTextOnly(groupActor, user.getVkId(), dialogState.getDialogPhrase().getDialogPhraseValue());
        greetingSetUpDialog.setIsFinished(true);
        userDialogRepository.save(greetingSetUpDialog);
    }
}
