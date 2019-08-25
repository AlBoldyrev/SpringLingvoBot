package com.vk.lingvobot.services.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.DialogState
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

    override fun handle(user: User, groupActor: GroupActor) {
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
        val dialogState = dialogStateRepository.findByDialogIdAndState(
            Dialogs.GREETING_SET_UP_DIALOG.value,
            greetingSetUpDialog.state
        )

        processDialog(user, groupActor, dialogState, greetingSetUpDialog)
    }

    //TODO Finish setup dialog
    private fun processDialog(
        user: User,
        groupActor: GroupActor,
        dialogState: DialogState,
        greetingSetUpDialog: UserDialog
    ) {
        when (dialogState.state) {
            1 -> {
                val buttons = mutableListOf<CustomButton>(CustomButton("Вы"), CustomButton("Ты"))
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
            }
            else -> messageService.sendMessageTextOnly(groupActor, user.userId, phrase!!.dialogPhraseValue)
        }
    }
}