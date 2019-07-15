package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.User;

public interface UserService {

    User findById(int id);
    User findByVkId(int vkId);
}
