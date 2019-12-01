package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.Settings
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.UserDialog
import com.vk.lingvobot.factory.AbstractFactory
import com.vk.lingvobot.repositories.*
import com.vk.lingvobot.services.SetupMessageService
import com.vk.lingvobot.states.SettingsSetupState
import com.vk.lingvobot.util.Dialogs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SetupMessageServiceImpl @Autowired constructor(
    private val dialogRepository: DialogRepository,
    private val dialogMaxStateRepository: DialogMaxStateRepository,
    private val dialogStateRepository: DialogStateRepository,
    private val userDialogRepository: UserDialogRepository,
    private val settingsRepository: SettingsRepository,
    private val stateFactory: AbstractFactory<SettingsSetupState>
) : SetupMessageService {

    private lateinit var settingsSetupState: SettingsSetupState

    private val difficultyLevel: Map<String, Int> = mapOf(Pair("Лёгкий", 1), Pair("Средний", 2), Pair("Сложный", 3))

    override fun handle(user: User, groupActor: GroupActor, messageBody: String) {
        var greetingSetUpDialog = userDialogRepository.findUserGreetingDialog(user.userId)

        val setupDialog = dialogRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.value)
        val setupDialogMaxState = dialogMaxStateRepository.findByDialogId(Dialogs.GREETING_SET_UP_DIALOG.value)

        if (greetingSetUpDialog == null) {
            greetingSetUpDialog = UserDialog(user, setupDialog, false, false)
            greetingSetUpDialog.state = 1
            userDialogRepository.save(greetingSetUpDialog)
        }

        var userSettings = settingsRepository.findBySettingsId(user.settings.settingsId)
        if (userSettings == null) {
            userSettings = Settings()
            user.settings = userSettings
        }


        val dialogToState = dialogStateRepository.findByDialogIdAndState(
            Dialogs.GREETING_SET_UP_DIALOG.value,
            greetingSetUpDialog.state
        )

        if (dialogToState.state <= setupDialogMaxState.dialogMaxStateValue) {
            settingsSetupState = stateFactory.getInstance(dialogToState.state)
            settingsSetupState.handle(user, groupActor, dialogToState, greetingSetUpDialog)
            processSettings(userSettings, messageBody)
        }
    }

    //Checks user answers. One of the first candidates for refactoring
    private fun processSettings(userSettings: Settings, messageBody: String) {
        when (messageBody) {
            "Ты", "Вы" -> {
                userSettings.pronoun = messageBody
                settingsRepository.save(userSettings)
            }
            "Лёгкий", "Средний", "Сложный" -> {
                userSettings.difficultyLevel = difficultyLevel[messageBody]
                settingsRepository.save(userSettings)
            }
            "1", "2", "3", "4", "5" -> {
                userSettings.lessonsPerDay = messageBody.toInt()
                settingsRepository.save(userSettings)
            }
            "Утром", "Днём", "Вечером" -> {
                userSettings.partOfTheDay = messageBody
                settingsRepository.save(userSettings)
            }
        }
    }
}