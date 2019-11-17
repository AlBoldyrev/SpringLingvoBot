package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.repositories.KeyboardRepository;
import com.vk.lingvobot.services.KeyboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeyboardServiceImpl implements KeyboardService {

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Override
    public com.vk.lingvobot.entities.Keyboard findByKeyboardId(int id) {
        com.vk.lingvobot.entities.Keyboard keyboard = keyboardRepository.findByKeyboardId(id);
        if (keyboard == null) {
            log.error("There is no keyboard with id: " + id);
            return null;
        }
        return keyboard;
    }
}
