package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.UserDialog;

public interface UserDialogService {

    UserDialog findById(int id);
    UserDialog findCurrentDialogOfUser(int userVkId);

}

