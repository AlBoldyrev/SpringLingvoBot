package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserDialogService {

    UserDialog findById(int id);
    UserDialog findCurrentDialogOfUser(int userVkId);
    Optional<UserDialog> get(Integer id);
    void create(UserDialog userDialog);

}

