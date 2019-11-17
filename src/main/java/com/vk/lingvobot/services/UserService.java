package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void save(User user);
    User findById(int id);
    User findByVkId(int vkId);
}
