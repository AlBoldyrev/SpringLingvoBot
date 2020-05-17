package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final DialogService dialogService;

    public Settings findById(Integer id) {
        Settings settings = settingsRepository.findBySettingsId(id);
        if (settings == null) {
            log.error("There is no settings with id: " + id);
            return null;
        }
        return settings;
    }

    /**
     * Create new user using his vkId.
     */
    public User createNewUser(int vkId) {

        log.info("There is no user with vk id: " + vkId + ". Creating new user...");

        User user = new User(vkId);

        Settings settings = new Settings();
        Settings saveSettings = settingsRepository.save(settings);

        user.setSettings(saveSettings);

        //TODO change hardcode
        user.setLevel(2);
        userRepository.save(user);
        dialogService.proceedTheDialog("GreetingDialog", vkId, "");
        return user;
    }
}
