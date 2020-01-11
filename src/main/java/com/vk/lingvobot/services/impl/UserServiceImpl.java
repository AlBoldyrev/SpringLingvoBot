package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Override
    public User findById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            log.warn("There is no user with id: " + id);
            return null;
        }
        return user;
    }

    @Override
    public User findByVkId(int vkId) {
        User user = userRepository.findByVkId(vkId);
        if (user == null) {
            log.warn("There is no user with VK ID: " + vkId);
            return null;
        }
        return user;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
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
        return userRepository.save(user);
    }


}
