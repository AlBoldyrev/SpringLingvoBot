package com.vk.lingvobot.application.levels;

import com.vk.lingvobot.entities.User;

public interface IResponseMessageBodyHandler {

    void handle(User user, String messageBody);
}
