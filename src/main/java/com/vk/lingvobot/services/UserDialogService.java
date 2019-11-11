package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

@Service
public interface UserDialogService {

    UserDialog findById(int id);
    UserDialog findCurrentDialogOfUser(int userVkId);

}

