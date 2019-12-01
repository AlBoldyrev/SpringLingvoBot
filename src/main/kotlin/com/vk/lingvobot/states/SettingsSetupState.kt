package com.vk.lingvobot.states

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.DialogState
import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.UserDialog

interface SettingsSetupState {
    fun handle(
        user: User,
        groupActor: GroupActor,
        dialogState: DialogState,
        greetingSetUpDialog: UserDialog
    )
}