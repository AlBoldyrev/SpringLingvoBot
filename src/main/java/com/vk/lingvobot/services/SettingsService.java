package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.repositories.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SettingsService {

    @Autowired
    SettingsRepository settingsRepository;

    public Settings findById(Integer id) {
        Settings settings = settingsRepository.findBySettingsId(id);
        if (settings == null) {
            log.error("There is no settings with id: " + id);
            return null;
        }
        return settings;
    }
}
