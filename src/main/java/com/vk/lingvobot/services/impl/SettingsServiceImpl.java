package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.services.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsRepository settingsRepository;

    @Override
    public Settings findById(Integer id) {
        Settings settings = settingsRepository.findBySettingsId(id);
        if (settings == null) {
            log.error("There is no settings with id: " + id);
            return null;
        }
        return settings;
    }
}
