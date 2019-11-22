package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.keyboard.CustomButton
import com.vk.lingvobot.services.MainDialogServiceKt
import com.vk.lingvobot.services.MessageServiceKt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MainDialogServiceKtImpl : MainDialogServiceKt {

    @Autowired
    private lateinit var messageService: MessageServiceKt

    override fun processMainDialog(user: User, groupActor: GroupActor, buttonList: List<String>) {

        val buttons = mutableListOf<CustomButton>()
        for (button in buttonList) {
            buttons.add(CustomButton(button))
        }
        messageService.sendMessageWithTextAndKeyboard(
            groupActor,
            user.vkId,
            "Выберите один из диалогов:",
            buttons
        )
        println("KOTLIN 4ever!!!!!")
    }
}