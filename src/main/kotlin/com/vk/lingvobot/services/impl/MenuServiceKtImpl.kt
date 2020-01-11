package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.objects.messages.KeyboardButtonColor
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.UserDialog
import com.vk.lingvobot.keyboard.CustomButton
import com.vk.lingvobot.keyboard.MenuButtons
import com.vk.lingvobot.entities.menu.MenuLevel
import com.vk.lingvobot.entities.menu.MenuStage
import com.vk.lingvobot.repositories.DialogRepository
import com.vk.lingvobot.repositories.MenuStageRepository
import com.vk.lingvobot.repositories.UserDialogRepository
import com.vk.lingvobot.services.MenuServiceKt
import com.vk.lingvobot.services.MessageServiceKt
import com.vk.lingvobot.services.PhraseService
import com.vk.lingvobot.services.UserDialogService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class MenuServiceKtImpl @Autowired constructor(
    private val messageService: MessageServiceKt,
    private val menuStageRepository: MenuStageRepository,
    private val dialogRepository: DialogRepository,
    private val userDialogRepository: UserDialogRepository,
    private val userDialogService: UserDialogService,
    private val phraseService: PhraseService
) :
    MenuServiceKt {

    private val mainMenuButtons =
        listOf(
            listOf(
                CustomButton(MenuButtons.PHRASES.value, color = KeyboardButtonColor.PRIMARY),
                CustomButton(MenuButtons.DIALOGS.value, color = KeyboardButtonColor.PRIMARY)
            )
        )

    override fun processMainDialog(user: User, groupActor: GroupActor, buttonList: List<String>) {

        val buttons = mutableListOf<CustomButton>()
        for (button in buttonList) {
            buttons.add(CustomButton(button))
        }
        /*messageService.sendMessageWithTextAndKeyboard(
            groupActor,
            user.vkId,
            "Выберите один из диалогов:",
            buttons
        )*/
    }

    override fun handle(user: User, messageBody: String, groupActor: GroupActor) {
        var menuStage = menuStageRepository.findByUser(user.userId)
        if (menuStage == null) {
            menuStage = MenuStage(user = user, menuLevel = MenuLevel.MAIN, currentDialogPage = 0)
            menuStage = menuStageRepository.save(menuStage)
        }

        when (menuStage?.menuLevel) {
            MenuLevel.MAIN -> callMainMenu(user, messageBody, menuStage, groupActor)
            MenuLevel.DIALOG -> callDialogMenu(user, messageBody, menuStage, groupActor)
            MenuLevel.PHRASE -> callPhraseMenu(user, messageBody, menuStage, groupActor)
        }
    }

    private fun callMainMenu(user: User, messageBody: String, menuStage: MenuStage, groupActor: GroupActor) {
        when (messageBody) {
            MenuButtons.DIALOGS.value -> {
                menuStage.menuLevel = MenuLevel.DIALOG
                menuStageRepository.save(menuStage)
                callDialogMenu(user, messageBody, menuStage, groupActor)
            }
            MenuButtons.PHRASES.value -> {
                menuStage.menuLevel = MenuLevel.PHRASE
                menuStageRepository.save(menuStage)
                callPhraseMenu(user, messageBody, menuStage, groupActor)
            }
            else -> messageService.sendMessageWithTextAndKeyboard(
                groupActor,
                user.vkId,
                "Выберите режим обучения",
                mainMenuButtons
            )
        }
    }

    private fun callDialogMenu(user: User, messageBody: String, menuStage: MenuStage, groupActor: GroupActor) {
        val allDialogs = dialogRepository.findAllDialogExceptSettingOne()

        when (messageBody) {
            MenuButtons.NEXT.value -> {
                val nextDialogPage = menuStage.currentDialogPage + 1
                sendDialogsKeyboard(user, nextDialogPage, groupActor)
                menuStage.currentDialogPage = nextDialogPage
                menuStageRepository.save(menuStage)
            }
            MenuButtons.BACK.value -> {
                val previousDialogPage = menuStage.currentDialogPage - 1
                sendDialogsKeyboard(user, previousDialogPage, groupActor)
                menuStage.currentDialogPage = previousDialogPage
                menuStageRepository.save(menuStage)
            }
            MenuButtons.HOME.value -> {
                menuStage.menuLevel = MenuLevel.MAIN
                menuStageRepository.save(menuStage)
                callMainMenu(user, messageBody, menuStage, groupActor)
            }
            else -> {
                if (allDialogs.any { it.dialogName == messageBody }) {
                    enterTheDialog(user, messageBody)
                    userDialogService.processCommonDialog(user, groupActor)
                } else {
                    sendDialogsKeyboard(user, 0, groupActor)
                    menuStage.currentDialogPage = 0
                    menuStageRepository.save(menuStage)
                }
            }
        }

    }

    private fun callPhraseMenu(user: User, messageBody: String, menuStage: MenuStage, groupActor: GroupActor) {
        enterTheDialog(user, messageBody)
    }

    private fun sendPhraseDialogKeyboard(user: User, groupActor: GroupActor) {

        val allUserDialogs = userDialogRepository.findAllUserDialogs(user.userId)

        val allButtons = mutableListOf<List<CustomButton>>()

        messageService.sendMessageWithTextAndKeyboard(
                groupActor,
                user.vkId,
                "Выберите один из диалогов",
                allButtons
        )
    }

    private fun sendDialogsKeyboard(user: User, pageNumber: Int, groupActor: GroupActor) {
        val allUserDialogs = userDialogRepository.findAllUserDialogs(user.userId)

        val allButtons = mutableListOf<List<CustomButton>>()
        val buttonsInRow = mutableListOf<CustomButton>()
        val navigationButtons = mutableListOf<CustomButton>()

        val page: Pageable = PageRequest.of(pageNumber, 5, Sort.by("dialogId").ascending())
        val dialogsPage = dialogRepository.findAllDialogExceptSettingOne(page)

        if (dialogsPage.content.isNotEmpty()) {
            dialogsPage.forEach { dialog ->
                val foundUserDialog = allUserDialogs.find { it.dialog == dialog }
                buttonsInRow += if (foundUserDialog != null && foundUserDialog.isFinished) {
                    CustomButton(dialog.dialogName, color = KeyboardButtonColor.POSITIVE)
                } else {
                    CustomButton(dialog.dialogName)
                }
            }
            allButtons += buttonsInRow
        }

        when {
            dialogsPage.isFirst -> {
                navigationButtons += CustomButton(MenuButtons.HOME.value, color = KeyboardButtonColor.PRIMARY)
                if (dialogsPage.hasNext()) {
                    navigationButtons += CustomButton(MenuButtons.NEXT.value, color = KeyboardButtonColor.PRIMARY)
                }
            }
            dialogsPage.isLast -> {
                navigationButtons += CustomButton(MenuButtons.BACK.value, color = KeyboardButtonColor.PRIMARY)
                navigationButtons += CustomButton(MenuButtons.HOME.value, color = KeyboardButtonColor.PRIMARY)
            }
            else -> {
                navigationButtons += CustomButton(MenuButtons.BACK.value, color = KeyboardButtonColor.PRIMARY)
                navigationButtons += CustomButton(MenuButtons.HOME.value, color = KeyboardButtonColor.PRIMARY)
                navigationButtons += CustomButton(MenuButtons.NEXT.value, color = KeyboardButtonColor.PRIMARY)
            }
        }
        allButtons += navigationButtons

        messageService.sendMessageWithTextAndKeyboard(
            groupActor,
            user.vkId,
            "Выберите один из диалогов",
            allButtons
        )
    }

    /**
     * User sends us name of the particular dialog via Keyboard and we create UserDialog object using this data
     */
    private fun enterTheDialog(user: User, message: String) {
        val dialog = dialogRepository.findByDialogName(message)
        if (dialog == null) {
            logger.error("dialog with unexisting name")
        } else {
            val userDialog = UserDialog(user, dialog, false, false)
            userDialog.state = 1
            userDialogService.create(userDialog)
        }
    }
}