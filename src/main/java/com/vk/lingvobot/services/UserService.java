package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;

    public User findById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            log.warn("There is no user with id: " + id);
            return null;
        }
        return user;
    }

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





}
