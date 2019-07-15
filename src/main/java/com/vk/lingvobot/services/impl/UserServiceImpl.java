package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
