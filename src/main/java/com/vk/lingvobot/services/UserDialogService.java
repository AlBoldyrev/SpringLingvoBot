package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserDialogService {

    UserDialog findById(int id);
    UserDialog findCurrentDialogOfUser(int userVkId);
    void processCommonDialog(User user, GroupActor groupActor);
    Optional<UserDialog> get(Integer id);
    void create(UserDialog userDialog);
    void processPhrasesPairDialog(User user);
}

