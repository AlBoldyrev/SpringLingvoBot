package com.vk.lingvobot.keyboards;

import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.SystemProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CustomJavaButton {

    private KeyboardButtonActionType type;
    private KeyboardButtonAction action = new KeyboardButtonAction();
    private KeyboardButtonColor color;

    public CustomJavaButton(String label, String payload) {
        type = KeyboardButtonActionType.TEXT;
        color = KeyboardButtonColor.DEFAULT;
        action.setType(KeyboardButtonActionType.TEXT);
        action.setLabel(label);
        action.setPayload(payload);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomJavaButton that = (CustomJavaButton) o;
        return Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action);
    }
}
