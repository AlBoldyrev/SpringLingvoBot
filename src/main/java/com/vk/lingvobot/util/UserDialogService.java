package com.vk.lingvobot.util;

import com.vk.lingvobot.entities.UserDialog;

public interface UserDialogService {

    UserDialog findById(int id);
    UserDialog findCurrentDialogOfUser(int userVkId);

}

