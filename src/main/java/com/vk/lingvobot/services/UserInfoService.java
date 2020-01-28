package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

    String getUserDomain(GroupActor groupActor, int userId);
    boolean checkIfUserWroteTheMessageBefore(int userId);
    boolean hasUserDialogInProcess(User user);
    UserDialog checkGreetingSetupDialog(User user);
    UserDialog findUserGreetingDialog(User user);
}
