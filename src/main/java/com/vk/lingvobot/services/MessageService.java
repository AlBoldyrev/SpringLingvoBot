package com.vk.lingvobot.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.util.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {

    private final VkApiClient apiClient;
    private final GroupActor groupActor;
    private final UserInfoService userInfo;
    private final Activity activity;

    private final Random random = new Random();

    public void sendMessageTextOnly(int userVkId, String message) {

        int howMuchShouldActivityMessageBeDisplayed = activity.howMuchShouldActivityMessageBeDisplayed(message);

        String userDomain = userInfo.getUserDomain(groupActor, userVkId);

        try {
            apiClient.messages().setActivity(groupActor).type("typing").userId(userVkId).peerId(userVkId).execute();
            TimeUnit.SECONDS.sleep(howMuchShouldActivityMessageBeDisplayed);
            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).message(message).randomId(random.nextInt()).domain(userDomain).execute();

        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        } catch (InterruptedException e) {
            log.error("I really do not know how sleep method can fail" + e.getStackTrace());
        }

    }

    public void sendMessageWithTextAndAttachments(int userVkId, String message, String attachments) {

        int howMuchShouldActivityMessageBeDisplayed = activity.howMuchShouldActivityMessageBeDisplayed(message);

        try {
            String userDomain = userInfo.getUserDomain(groupActor, userVkId);

            apiClient.messages().setActivity(groupActor).type("typing").userId(userVkId).peerId(userVkId).execute();
            TimeUnit.SECONDS.sleep(howMuchShouldActivityMessageBeDisplayed);

            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).message(message).randomId(random.nextInt())
                    .domain(userDomain).attachment(attachments).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        } catch (InterruptedException e) {
            log.error("I really do not know how sleep method can fail" + e.getStackTrace());
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

    public void sendMessageWithTextAndKeyboard(int userVkId, String message, Keyboard keyboard) {

        int howMuchShouldActivityMessageBeDisplayed = activity.howMuchShouldActivityMessageBeDisplayed(message);

        try {
            String userDomain = userInfo.getUserDomain(groupActor, userVkId);

            apiClient.messages().setActivity(groupActor).type("typing").userId(userVkId).peerId(userVkId).execute();
            TimeUnit.SECONDS.sleep(howMuchShouldActivityMessageBeDisplayed);

            apiClient.messages().send(groupActor).peerId(userVkId).userIds(userVkId).randomId(random.nextInt())
                    .domain(userDomain).message(message).keyboard(keyboard).execute();
        } catch (ApiException e) {
            log.error("Something wrong with API: " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("Something wrong with CLIENT: " + e.getStackTrace());
        } catch (InterruptedException e) {
            log.error("I really do not know how sleep method can fail" + e.getStackTrace());
        }
    }
}

