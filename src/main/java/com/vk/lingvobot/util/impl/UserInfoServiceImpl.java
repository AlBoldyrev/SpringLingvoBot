package com.vk.lingvobot.util.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.parser.Parser;
import com.vk.lingvobot.parser.Response;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.util.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private UserRepository userRepository;

    public String getUserDomain(GroupActor groupActor, int userId) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String domainName = null;
        try {
            domainName = apiClient.users().get(groupActor).userIds(String.valueOf(userId)).fields(Fields.DOMAIN).executeAsString();
        } catch (ClientException e) {
            log.error("Can not get userDomain :( " + e.getMessage());
        }
        Parser parser = gson.fromJson(domainName, Parser.class);
        List<Response> response = parser.getResponses();

        return response.get(0).getDomain();
    }

    public boolean checkIfUserWroteTheMessageBefore(int userVkId) {

        User user = userRepository.findByVkId(userVkId);
        return user != null;
    }
}
