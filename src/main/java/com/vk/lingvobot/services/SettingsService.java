package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Settings;

public interface SettingsService {

    Settings findById(Integer id);
}
