package com.vk.lingvobot.util;

import com.vk.api.sdk.client.actors.GroupActor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface UserInfoService {

    String getUserDomain(GroupActor groupActor, int userId);
    boolean checkIfUserWroteTheMessageBefore(int userId);
}
