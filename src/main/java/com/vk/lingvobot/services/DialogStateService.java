package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.DialogState;

public interface DialogStateService {

    DialogState findById(Integer id);
}
