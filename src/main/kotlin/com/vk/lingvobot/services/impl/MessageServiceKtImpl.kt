package com.vk.lingvobot.services.impl

import com.vk.api.sdk.actions.Messages
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.objects.messages.*
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
        stringKeyboard: String
    ) {
        val randomId = Random.nextInt()
        val btnAction = KeyboardButtonAction().setType(KeyboardButtonActionType.TEXT).setLabel("PIECE OF KEYBOARD!")
            .setPayload("{\"button\":\"4\"}")
        val button = KeyboardButton().setColor(KeyboardButtonColor.DEFAULT).setAction(btnAction)
        val keyboard = Keyboard()
        keyboard.oneTime = true
        keyboard.buttons = mutableListOf(mutableListOf(button))
        vkApiClient.messages().send(groupActor).message(message).userId(userId).randomId(randomId).keyboard(keyboard)
            .execute()
    }


}