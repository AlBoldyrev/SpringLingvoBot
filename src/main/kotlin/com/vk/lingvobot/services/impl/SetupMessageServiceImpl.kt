package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.DialogState
import com.vk.lingvobot.entities.Settings
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.UserDialog
import com.vk.lingvobot.keyboard.CustomButton
import com.vk.lingvobot.repositories.*
import com.vk.lingvobot.services.MessageServiceKt
import com.vk.lingvobot.services.SetupMessageService
import com.vk.lingvobot.util.Dialogs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SetupMessageServiceImpl : SetupMessageService {

    @Autowired
    private lateinit var dialogRepository: DialogRepository

    @Autowired
    private lateinit var dialogMaxStateRepository: DialogMaxStateRepository

    @Autowired
    private lateinit var dialogStateRepository: DialogStateRepository

    @Autowired
    private lateinit var dialogPhrasesRepository: DialogPhraseRepository

    @Autowired
    private lateinit var userDialogRepository: UserDialogRepository

    @Autowired
    private lateinit var messageService: MessageServiceKt

    @Autowired
    private lateinit var settingsRepository: SettingsRepository

    override fun handle(user: User, groupActor: GroupActor, messageBody: String) {
        var greetingSetUpDialog = userDialogRepository.findCanceledDialogByUserIdAndDialogId(
                user.userId,
                Dialogs.GREETING_SET_UP_DIALOG.value
        )

        val setupDialog = dialogRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.value)
        val setupDialogMaxState = dialogMaxStateRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.value)

        if (greetingSetUpDialog == null) {
            greetingSetUpDialog = UserDialog(user, setupDialog, false, false)
            greetingSetUpDialog.state = 1
        }

        var userSettings = settingsRepository.findBySettingsId(user.settings.settingsId)
        if (userSettings == null) {
            userSettings = Settings()
            userSettings.user = user
            user.settings = userSettings
        }


        val dialogToState = dialogStateRepository.findByDialogIdAndState(
                Dialogs.GREETING_SET_UP_DIALOG.value,
                greetingSetUpDialog.state
        )

        if (dialogToState.state <= setupDialogMaxState.dialogMaxStateValue) {
            processDialog(user, groupActor, dialogToState, greetingSetUpDialog)
            processSettings(userSettings, messageBody)
        }
    }

    private fun processDialog(
            user: User,
            groupActor: GroupActor,
            dialogState: DialogState,
            greetingSetUpDialog: UserDialog
    ) {
        when (dialogState.state) {
            1 -> {
                val buttons = mutableListOf(CustomButton("Вы"), CustomButton("Ты"))
                messageService.sendMessageWithTextAndKeyboard(
                        groupActor,
                        user.userId,
                        dialogState.dialogPhrase.dialogPhraseValue,
                        buttons
                )
                greetingSetUpDialog.state += 1
                userDialogRepository.save(greetingSetUpDialog)
            }
            5 -> {
                val buttons = mutableListOf(CustomButton("Лёгкий"), CustomButton("Средний"), CustomButton("Сложный"))
                messageService.sendMessageWithTextAndKeyboard(
                        groupActor,
                        user.userId,
                        dialogState.dialogPhrase.dialogPhraseValue,
                        buttons
                )
                greetingSetUpDialog.state += 1
                userDialogRepository.save(greetingSetUpDialog)
            }
            6 -> {
                val buttons = mutableListOf(CustomButton("1"),
                        CustomButton("2"),
                        CustomButton("3"),
                        CustomButton("4"),
                        CustomButton("5"))
                messageService.sendMessageWithTextAndKeyboard(
                        groupActor,
                        user.userId,
                        dialogState.dialogPhrase.dialogPhraseValue,
                        buttons
                )
                greetingSetUpDialog.state += 1
                userDialogRepository.save(greetingSetUpDialog)
            }
            7 -> {
                val buttons = mutableListOf(CustomButton("Утром"),
                        CustomButton("Днём"),
                        CustomButton("Вечером"))
                messageService.sendMessageWithTextAndKeyboard(
                        groupActor,
                        user.userId,
                        dialogState.dialogPhrase.dialogPhraseValue,
                        buttons
                )
                greetingSetUpDialog.state += 1
                userDialogRepository.save(greetingSetUpDialog)
            }
            8 -> {
                greetingSetUpDialog.isFinished = true
                userDialogRepository.save(greetingSetUpDialog)
            }
            else -> {
                messageService.sendMessageTextOnly(
                        groupActor,
                        user.userId,
                        dialogState.dialogPhrase.dialogPhraseValue
                )
                greetingSetUpDialog.state += 1
                userDialogRepository.save(greetingSetUpDialog)
            }
        }
    }

    //Checks user answers. One of the first candidates for refactoring
    private fun processSettings(userSettings: Settings, messageBody: String) {
        when (messageBody) {
            "Ты" -> {
                userSettings.pronoun = messageBody
                settingsRepository.save(userSettings)
            }
            "Вы" -> {
                userSettings.pronoun = messageBody
                settingsRepository.save(userSettings)
            }
            "Лёгкий" -> {
                userSettings.difficultyLevel = 1
                settingsRepository.save(userSettings)
            }
            "Средний" -> {
                userSettings.difficultyLevel = 2
                settingsRepository.save(userSettings)
            }
            "Сложный" -> {
                userSettings.difficultyLevel = 3
                settingsRepository.save(userSettings)
            }
            "1" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "2" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "3" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "4" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "5" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "Утром" -> {
                userSettings.partOfTheDay = messageBody
                settingsRepository.save(userSettings)
            }
            "Днём" -> {
                userSettings.partOfTheDay = messageBody
                settingsRepository.save(userSettings)
            }
            "Вечером" -> {
                userSettings.partOfTheDay = messageBody
                settingsRepository.save(userSettings)
            }
        }
    }
}