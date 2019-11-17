package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Keyboard;
import org.springframework.stereotype.Service;

@Service
public interface KeyboardService {

    Keyboard findByKeyboardId(int id);
}
