package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.keyboard.CustomButton
import com.vk.lingvobot.keyboard.MenuButtons
import com.vk.lingvobot.menu.MenuLevel
import com.vk.lingvobot.repositories.MenuStageRepository
import com.vk.lingvobot.services.MenuServiceKt
import com.vk.lingvobot.services.MessageServiceKt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MenuServiceKtImpl @Autowired constructor(
    private val messageService: MessageServiceKt,
    private val menuStageRepository: MenuStageRepository
) :
    MenuServiceKt {

    private val mainMenuButtons =
        listOf(CustomButton(MenuButtons.PHRASES.value), CustomButton(MenuButtons.DIALOGS.value))

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
    }

    override fun handle(user: User, messageBody: String, groupActor: GroupActor) {
        val menuStage = menuStageRepository.findByUser(user.userId)

        when (menuStage.menuLevel) {
            MenuLevel.MAIN -> callMainMenu(user, messageBody, groupActor)
            MenuLevel.DIALOG -> callDialogMenu()
            MenuLevel.PHRASE -> callPhraseMenu()
        }
    }

    private fun callMainMenu(user: User, messageBody: String, groupActor: GroupActor) {
        when (messageBody) {
            MenuButtons.DIALOGS.value -> Unit
            MenuButtons.PHRASES.value -> {

            }
            else -> messageService.sendMessageWithTextAndKeyboard(
                groupActor,
                user.vkId,
                "Выберите режим обучения",
                mainMenuButtons
            )
        }
    }

    private fun callDialogMenu() {

    }

    private fun callPhraseMenu() {

    }
}