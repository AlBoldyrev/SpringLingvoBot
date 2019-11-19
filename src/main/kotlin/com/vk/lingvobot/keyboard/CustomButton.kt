package com.vk.lingvobot.keyboard

import com.vk.api.sdk.objects.messages.KeyboardButtonActionType
import com.vk.api.sdk.objects.messages.KeyboardButtonColor

data class CustomButton(
    val label: String,
    var type: KeyboardButtonActionType = KeyboardButtonActionType.TEXT,
    var color: KeyboardButtonColor = KeyboardButtonColor.DEFAULT,
    var payload: String = ""
)