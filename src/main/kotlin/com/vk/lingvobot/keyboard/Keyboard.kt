package com.vk.lingvobot.keyboard

import com.vk.api.sdk.objects.messages.Keyboard
import com.vk.api.sdk.objects.messages.KeyboardButton
import com.vk.api.sdk.objects.messages.KeyboardButtonAction

fun getButton(
    button: CustomButton
): KeyboardButton {
    val btnAction = KeyboardButtonAction().setType(button.type).setLabel(button.label)
        .setPayload(button.payload)
    return KeyboardButton().setColor(button.color).setAction(btnAction)
}

fun getKeyboard(buttons: List<List<KeyboardButton>>): Keyboard {
    val keyboard = Keyboard()
    keyboard.oneTime = true
    keyboard.buttons = buttons

    return keyboard
}


fun Keyboard.addButton(button: KeyboardButton) {
    buttons.getOrNull(0)?.add(button)
}