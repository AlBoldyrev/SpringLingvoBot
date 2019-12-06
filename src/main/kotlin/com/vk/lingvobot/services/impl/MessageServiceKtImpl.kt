package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.objects.messages.*
import com.vk.lingvobot.keyboard.CustomButton
import com.vk.lingvobot.keyboard.getButton
import com.vk.lingvobot.keyboard.getKeyboard
import com.vk.lingvobot.services.MessageServiceKt
import com.vk.lingvobot.util.chop
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class MessageServiceKtImpl : MessageServiceKt {

    @Autowired
    private lateinit var vkApiClient: VkApiClient

    override fun sendMessageTextOnly(groupActor: GroupActor, userVkId: Int, message: String) {
        val randomId = Random.nextInt()
        vkApiClient.messages().send(groupActor).message(message).userId(userVkId).randomId(randomId).execute()
    }

    override fun sendMessageWithTextAndAttachments(userVkId: Int, message: String, attachments: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithAttachmentsAndKeyboard(userVkId: Int, attachments: String, keyboard: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithAttachmentsOnly(userVkId: Int, attachments: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessageWithTextAndKeyboard(
        groupActor: GroupActor,
        userVkId: Int,
        message: String,
        keyboardButtons: List<List<CustomButton>>
    ) {
        val randomId = Random.nextInt()
        val buttons = addButtons(keyboardButtons)
        val keyboard = getKeyboard(buttons)
        vkApiClient.messages().send(groupActor).message(message).userId(userVkId).randomId(randomId).keyboard(keyboard)
            .execute()
    }

    private fun addButtons(buttonsToAdd: List<List<CustomButton>>): List<List<KeyboardButton>> {
        val buttons = mutableListOf<MutableList<KeyboardButton>>()
        for (buttonListToAdd in buttonsToAdd) {
            val listToAdd = mutableListOf<KeyboardButton>()
            for (buttonToAdd in buttonListToAdd) {
                val button = getButton(buttonToAdd)
                listToAdd += button
            }
            buttons += listToAdd
        }
        return buttons
    }
}