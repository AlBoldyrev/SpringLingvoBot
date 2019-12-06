package com.vk.lingvobot.services

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.User

interface MenuServiceKt {

    fun handle(user: User, messageBody: String, groupActor: GroupActor)
    fun processMainDialog(user: User, groupActor: GroupActor, buttonList: List<String>)
//    fun callMainMenu(user: User, messageBody: String, groupActor: GroupActor)
//    fun callDialogListMenu(user: User, groupActor: GroupActor)
}