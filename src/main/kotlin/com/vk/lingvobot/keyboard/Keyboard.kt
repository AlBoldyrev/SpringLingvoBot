package com.vk.lingvobot.keyboard

import com.vk.api.sdk.objects.messages.*

fun getButton(
    button: CustomButton
): KeyboardButton {
    val btnAction = KeyboardButtonAction().setType(button.type).setLabel(button.label)
        .setPayload(button.payload)
    return KeyboardButton().setColor(button.color).setAction(btnAction)
}

fun getKeyboard(buttons: List<KeyboardButton>): Keyboard {
    val keyboard = Keyboard()
    keyboard.oneTime = true
    keyboard.buttons = mutableListOf(buttons)

    return keyboard
}


fun Keyboard.addButton(button: KeyboardButton) {
    buttons.getOrNull(0)?.add(button)
}