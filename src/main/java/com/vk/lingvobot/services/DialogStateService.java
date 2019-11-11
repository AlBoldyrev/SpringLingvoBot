package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.DialogState;
import org.springframework.stereotype.Service;

@Service
public interface DialogStateService {

    DialogState findById(Integer id);
}
