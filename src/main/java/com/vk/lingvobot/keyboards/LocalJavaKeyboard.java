package com.vk.lingvobot.keyboards;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.lingvobot.util.ListChopper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LocalJavaKeyboard {

    /**
     * This method created Keyboards with buttons: 5 button max in row. If you need to have more buttons - user another method.
     * @param buttonNames
     * @return Keyboard object legit to use in original VK methods
     */
    public Keyboard createKeyboardWithButtonsBrickByBrick(List<String> buttonNames) {

        List<KeyboardButton> keyboardButtons = convertStringsIntoKeyboardButton(buttonNames);
        Keyboard keyboard = new Keyboard();
        for (KeyboardButton keyboardButton: keyboardButtons) {
            List<List<KeyboardButton>> oneButtonInRowListOfLists = ListChopper.chop(keyboardButtons, keyboardButtons.size());
            keyboard.setButtons(oneButtonInRowListOfLists);
            keyboard.setOneTime(true);
        }
        return keyboard;
    }

    /**
     * This method created Keyboards with buttons: one button - one row.
     * @param buttonNames
     * @return Keyboard object legit to use in original VK methods
     */
    public Keyboard createKeyboardWithButtonsOneButtonOneRow(List<String> buttonNames) {

        List<KeyboardButton> keyboardButtons = convertStringsIntoKeyboardButton(buttonNames);
        Keyboard keyboard = new Keyboard();
        for (KeyboardButton keyboardButton: keyboardButtons) {
            List<List<KeyboardButton>> oneButtonInRowListOfLists = ListChopper.chop(keyboardButtons, 1);
            keyboard.setButtons(oneButtonInRowListOfLists);
            keyboard.setOneTime(true);
        }
        return keyboard;
    }

    /**
     * Method takes button names and convert it into original VK object @KeyboardButton
     *
     * @param buttonNames
     * @return
     */
    private List<KeyboardButton> convertStringsIntoKeyboardButton(List<String> buttonNames) {

        List<CustomJavaButton> customJavaButtons = convertStringsToCustomJavaButtons(buttonNames);

        List<KeyboardButton> keyboardButtons = new ArrayList<>();
        for (CustomJavaButton customJavaButton: customJavaButtons) {
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setAction(customJavaButton.getAction());
            keyboardButton.setColor(customJavaButton.getColor());
            keyboardButtons.add(keyboardButton);
        }
        return keyboardButtons;
    }

    /**
     * Method takes button names and convert it into our @CustomJavaButton object
     *
     * @param buttonNames
     * @return
     */
    private List<CustomJavaButton> convertStringsToCustomJavaButtons(List<String> buttonNames) {
        List<CustomJavaButton> customJavaButtons = new ArrayList<>();
        for (String buttonName: buttonNames) {
            CustomJavaButton customJavaButton = new CustomJavaButton(buttonName, "");
            customJavaButtons.add(customJavaButton);
        }
        return customJavaButtons;
    }
}
