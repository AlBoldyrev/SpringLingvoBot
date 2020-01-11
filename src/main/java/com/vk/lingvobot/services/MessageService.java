package com.vk.lingvobot.services;

import com.vk.api.sdk.objects.messages.Keyboard;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {

    void sendMessageTextOnly(int userId, String message);
    void sendMessageWithTextAndAttachments(int userId, String message, String attachments);
    void sendMessageWithAttachmentsAndKeyboard(int userId, String attachments, Keyboard keyboard);
    void sendMessageWithAttachmentsOnly(int userId, String attachments);
    void sendMessageWithTextAndKeyboard(int userId, String message, Keyboard keyboard);

}
