package com.vk.lingvobot.util;

import com.vk.lingvobot.entities.User;

public interface UserService {

    User findById(int id);
    User findByVkId(int vkId);
}
