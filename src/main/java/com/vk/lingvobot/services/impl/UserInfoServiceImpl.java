package com.vk.lingvobot.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.parser.Parser;
import com.vk.lingvobot.parser.Response;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.UserInfoService;
import com.vk.lingvobot.util.Dialogs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDialogRepository userDialogRepository;

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

    /**
     * Check if vk user is registered
     * @param vkUserId
     * @return {@link User}
     */
    public User isExists(int vkUserId) {
        return userRepository.findByVkId(vkUserId);
    }

    /**
     * Check if {@param user} completed initial set up
     */
    public UserDialog checkGreetingSetupDialog(User user) {
        if (user == null) return null;
        UserDialog dialog = userDialogRepository.findFinishedDialogByUserIdAndDialogId(user.getUserId(), Dialogs.GREETING_SET_UP_DIALOG.getValue());

        return dialog;
    }
}
