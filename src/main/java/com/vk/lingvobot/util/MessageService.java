package com.vk.lingvobot.util;

public interface MessageService {

    void sendMessageTextOnly(int userId, String message);
    void sendMessageWithTextAndAttachements(int userId, String message, String attachments);
    void sendMessageWithAttachmentsAndKeyboard(int userId, String attachments, String keyboard);
    void sendMessageWithAttachmentsOnly(int userId, String attachments);

}
