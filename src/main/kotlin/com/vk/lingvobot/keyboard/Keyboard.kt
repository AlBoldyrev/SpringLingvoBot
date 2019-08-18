package com.vk.lingvobot.keyboard

import com.vk.api.sdk.objects.messages.*

fun getButton(
    label: String,
    type: KeyboardButtonActionType = KeyboardButtonActionType.TEXT,
    color: KeyboardButtonColor = KeyboardButtonColor.DEFAULT,
    payload: String = ""
): KeyboardButton {
    val btnAction = KeyboardButtonAction().setType(type).setLabel(label)
        .setPayload(payload)
    return KeyboardButton().setColor(KeyboardButtonColor.DEFAULT).setAction(btnAction)
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