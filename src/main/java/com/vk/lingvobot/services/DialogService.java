package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.stereotype.Service;

@Service
public interface DialogService {

    Dialog findById(Integer id);
}
