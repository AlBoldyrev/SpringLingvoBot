package com.vk.lingvobot.services

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.keyboard.CustomButton

interface MessageServiceKt {

    fun sendMessageTextOnly(groupActor: GroupActor, userVkId: Int, message: String)
    fun sendMessageWithTextAndAttachments(groupActor: GroupActor, userVkId: Int, message: String, attachments: String)
    fun sendMessageWithAttachmentsAndKeyboard(userVkId: Int, attachments: String, keyboard: String)
    fun sendMessageWithAttachmentsOnly(userVkId: Int, attachments: String)
    fun sendMessageWithTextAndKeyboard(
        groupActor: GroupActor,
        userVkId: Int,
        message: String,
        keyboardButtons: List<List<CustomButton>>
    )

}