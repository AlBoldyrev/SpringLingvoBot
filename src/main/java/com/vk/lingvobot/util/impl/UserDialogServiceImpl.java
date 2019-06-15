package com.vk.lingvobot.util.impl;

import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.util.UserDialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDialogServiceImpl implements UserDialogService {

    @Autowired
    private UserDialogRepository userDialogRepository;

    @Override
    public UserDialog findById(int id) {
        UserDialog userDialog = userDialogRepository.findById(id);
        if (userDialog == null) {
            log.warn("There is no userDialog with id: " + id);
            return null;
        }
        return userDialog;
    }

    @Override
    public UserDialog findCurrentDialogOfUser(int userVkId) {
        UserDialog userDialog = userDialogRepository.findCurrentDialogOfUser(userVkId);
        if (userDialog == null) {
            log.warn("There is no userDialog with userVkId: " + userVkId);
            return null;
        }
        return userDialog;
    }
}
