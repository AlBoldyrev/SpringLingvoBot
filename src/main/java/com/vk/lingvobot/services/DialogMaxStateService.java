package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.DialogMaxState;
import org.springframework.stereotype.Service;

@Service
public interface DialogMaxStateService {

    DialogMaxState findById(Integer id);
}
