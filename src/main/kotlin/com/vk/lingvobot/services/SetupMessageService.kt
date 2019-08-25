package com.vk.lingvobot.services

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.lingvobot.entities.User

interface SetupMessageService {

    fun handle(user: User, groupActor: GroupActor)
}