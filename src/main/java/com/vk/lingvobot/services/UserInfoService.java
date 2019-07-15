package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import org.springframework.stereotype.Component;

@Component
public interface UserInfoService {

    String getUserDomain(GroupActor groupActor, int userId);
    boolean checkIfUserWroteTheMessageBefore(int userId);
}
