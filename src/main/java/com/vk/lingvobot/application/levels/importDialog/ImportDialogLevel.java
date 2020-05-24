package com.vk.lingvobot.application.levels.importDialog;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.UserRepository;
import com.vk.lingvobot.services.ImportDialogService;
import com.vk.lingvobot.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportDialogLevel implements IResponseMessageBodyHandler {

    private final ImportDialogService importDialogService;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();
        if (message.getObject().getAttachments().size() > 1) {
            messageService.sendMessageTextOnly(user.getVkId(), "Пожалуйста, пришли мне всего один файл. Больше не надо...");
        } else {
            String url = message.getObject().getAttachments().get(0).getDocumnent().getUrl();
            try {
                URL trueURL = new URL(url);
                byte[] bytes = downloadUrl(trueURL);
                importDialogService.importDialog(bytes);
                user.setLevel(MenuLevel.MAIN.getCode());
                actionLevel1(user.getVkId());
            } catch (MalformedURLException e) {
                System.out.println("Baaaaad url, Sasha. Baaaaaaad url.");
            }

            System.out.println("LOL KONECHNO");
        }

        if (messageBody.equals("BACK")) {
            user.setLevel(MenuLevel.MAIN.getCode());
            userRepository.save(user);
            actionLevel1(user.getVkId());
        }

        userRepository.save(user);
    }

    private void actionLevel1(int userVkId) {
        List<String> levelFirst = new ArrayList<>();
        levelFirst.add("Phrases");
        levelFirst.add("Dialogs");
        levelFirst.add("Import dialog");
        Keyboard keyboardWithButtonsBrickByBrick = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(levelFirst);
        messageService.sendMessageWithTextAndKeyboard(userVkId, "Дружище, ты в главном меню.", keyboardWithButtonsBrickByBrick);
    }


    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }
}
