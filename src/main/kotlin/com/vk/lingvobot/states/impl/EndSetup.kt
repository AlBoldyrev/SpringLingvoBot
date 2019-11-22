package com.vk.lingvobot.states.impl

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.DialogState
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.UserDialog
import com.vk.lingvobot.repositories.UserDialogRepository
import com.vk.lingvobot.services.MessageServiceKt
import com.vk.lingvobot.states.SettingsSetupState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EndSetup @Autowired constructor(
    private val messageService: MessageServiceKt,
    private val userDialogRepository: UserDialogRepository
) : SettingsSetupState {

    override fun handle(
        user: User,
        groupActor: GroupActor,
        dialogState: DialogState,
        greetingSetUpDialog: UserDialog
    ) {
        messageService.sendMessageTextOnly(
            groupActor,
            user.vkId,
            dialogState.dialogPhrase.dialogPhraseValue
        )
        greetingSetUpDialog.isFinished = true
        userDialogRepository.save(greetingSetUpDialog)
    }
}