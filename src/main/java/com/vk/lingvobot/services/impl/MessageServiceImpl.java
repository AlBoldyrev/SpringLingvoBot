package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import com.vk.lingvobot.services.MessageService;

@Component
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private VkApiClient apiClient;

    @Autowired
    private GroupActor groupActor;

    @Autowired
    private UserInfoServiceImpl userInfo;

    private final Random random = new Random();

    public void sendMessageTextOnly(int userVkId, String message) {

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);
        try {
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).message(message).randomId(random.nextInt()).domain(userDomain).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }

    }

    public void sendMessageWithTextAndAttachements(int userVkId, String message, String attachments) {

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);
        try {
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).message(message).randomId(random.nextInt()).domain(userDomain).attachment(attachments).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }
    }

    public void sendMessageWithAttachmentsAndKeyboard(int userVkId, String attachments, Keyboard keyboard) {

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);
        try {
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).randomId(random.nextInt())
                    .domain(userDomain).message(" ").attachment(attachments).keyboard(keyboard).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }
    }

    public void sendMessageWithAttachmentsOnly(int userVkId, String attachments) {

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);
        try {
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).randomId(random.nextInt())
                    .domain(userDomain).message("    ").attachment(attachments).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }

    }

    @Override
    public void sendMessageWithTextAndKeyboard(int userVkId, String message, Keyboard keyboard) {

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);
        try {
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).randomId(random.nextInt())
                    .domain(userDomain).message(message).keyboard(keyboard).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }
    }

}