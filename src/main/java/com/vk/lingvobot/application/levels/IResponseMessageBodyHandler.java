package com.vk.lingvobot.application.levels;

import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;

public interface IResponseMessageBodyHandler  {

    void handle(User user, ModelMessageNew message);

}
