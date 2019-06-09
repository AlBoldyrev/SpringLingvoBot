package com.vk.lingvobot.util;

import com.vk.api.sdk.client.actors.GroupActor;

public interface UserInfoService {

    String getUserDomain(GroupActor groupActor, int userId);
}
