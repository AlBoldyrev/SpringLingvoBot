package com.vk.lingvobot.util.impl;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.lingvobot.util.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

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

    public void sendMessageTextOnly(int userId, String message) {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }

    }

    public void sendMessageWithTextAndAttachements(int userId, String message, String attachments) {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).message(message).randomId(random.nextInt()).domain(userDomain).attachment(attachments).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }
    }

    public void sendMessageWithAttachmentsAndKeyboard(int userId, String attachments, String keyboard) {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                    .domain(userDomain).message(" ").attachment(attachments).unsafeParam("keyboard", keyboard).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }
    }

    public void sendMessageWithAttachmentsOnly(int userId, String attachments) {

        String userDomain = userInfo.getUserDomain(groupActor, userId);
        try {
            apiClient.messages().send(groupActor).peerId(userId).userIds(userId).randomId(random.nextInt())
                    .domain(userDomain).message("    ").attachment(attachments).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        }

    }

}
