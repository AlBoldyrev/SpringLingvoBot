package com.vk.lingvobot.services

import com.vk.api.sdk.client.actors.GroupActor

interface MessageServiceKt {

    fun sendMessageTextOnly(groupActor: GroupActor, userId: Int, message: String)
    fun sendMessageWithTextAndAttachments(userId: Int, message: String, attachments: String)
    fun sendMessageWithAttachmentsAndKeyboard(userId: Int, attachments: String, keyboard: String)
    fun sendMessageWithAttachmentsOnly(userId: Int, attachments: String)
    fun sendMessageWithTextAndKeyboard(
        groupActor: GroupActor,
        userId: Int,
        message: String,
        keyboardLabels: List<String>
    )

}