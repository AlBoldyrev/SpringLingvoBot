package com.vk.lingvobot.keyboards;

import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Data
public class CustomJavaButton {

    private KeyboardButtonActionType type;
    private KeyboardButtonAction action = new KeyboardButtonAction();
    private KeyboardButtonColor color;

    CustomJavaButton(String label, String payload) {
        type = KeyboardButtonActionType.TEXT;
        color = KeyboardButtonColor.DEFAULT;
        action.setType(KeyboardButtonActionType.TEXT);
        action.setLabel(label);
        action.setPayload(payload);
    }
}
