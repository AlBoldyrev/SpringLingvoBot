package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

    String getUserDomain(GroupActor groupActor, int userId);
    boolean checkIfUserWroteTheMessageBefore(int userId);
}
