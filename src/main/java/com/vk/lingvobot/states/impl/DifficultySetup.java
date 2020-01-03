package com.vk.lingvobot.states.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.keyboard.CustomButton;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.states.SettingsSetupState;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DifficultySetup implements SettingsSetupState {

    private final MessageServiceKt messageService;
    private final UserDialogRepository userDialogRepository;

    @Override
    public void handle(User user, GroupActor groupActor, DialogState dialogState, UserDialog greetingSetUpDialog) {
        List<List<CustomButton>> buttons = new ArrayList<>();
        List<CustomButton> customButtons = new ArrayList<>();
        customButtons.add(new CustomButton("Лёгкий", KeyboardButtonActionType.TEXT, KeyboardButtonColor.DEFAULT, ""));
        customButtons.add(new CustomButton("Средний", KeyboardButtonActionType.TEXT, KeyboardButtonColor.DEFAULT, ""));
        customButtons.add(new CustomButton("Сложный", KeyboardButtonActionType.TEXT, KeyboardButtonColor.DEFAULT, ""));
        buttons.add(customButtons);
        messageService.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(),
                dialogState.getDialogPhrase().getDialogPhraseValue(), buttons);
        greetingSetUpDialog.setState(greetingSetUpDialog.getState() + 1);
        userDialogRepository.save(greetingSetUpDialog);
    }
}
