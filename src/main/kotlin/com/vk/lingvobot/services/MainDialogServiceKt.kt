package com.vk.lingvobot.services

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.User

interface MainDialogServiceKt {

    fun processMainDialog(user: User, groupActor: GroupActor, buttonList: List<String>)
}