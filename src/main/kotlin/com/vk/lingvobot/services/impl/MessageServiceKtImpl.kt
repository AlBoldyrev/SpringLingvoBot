package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.objects.messages.*
import com.vk.lingvobot.keyboard.getButton
import com.vk.lingvobot.keyboard.getKeyboard
import com.vk.lingvobot.services.MessageServiceKt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class MessageServiceKtImpl : MessageServiceKt {

    @Autowired
    private lateinit var vkApiClient: VkApiClient

    override fun sendMessageTextOnly(groupActor: GroupActor, userId: Int, message: String) {
        val randomId = Random.nextInt()
        vkApiClient.messages().send(groupActor).message(message).userId(userId).randomId(randomId).execute()
    }

    override fun sendMessageWithTextAndAttachments(userId: Int, message: String, attachments: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithAttachmentsAndKeyboard(userId: Int, attachments: String, keyboard: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithAttachmentsOnly(userId: Int, attachments: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithTextAndKeyboard(
        groupActor: GroupActor,
        userId: Int,
        message: String,
        keyboardLabels: List<String>
    ) {
        val randomId = Random.nextInt()
        val buttons = addButtons(keyboardLabels)
        val keyboard = getKeyboard(buttons)
        vkApiClient.messages().send(groupActor).message(message).userId(userId).randomId(randomId).keyboard(keyboard)
            .execute()
    }

    private fun addButtons(labels: List<String>): List<KeyboardButton> {
        val buttons = mutableListOf<KeyboardButton>()
        for (label in labels) {
            val button = getButton(label)
            buttons.add(button)
        }
        return buttons
    }
}