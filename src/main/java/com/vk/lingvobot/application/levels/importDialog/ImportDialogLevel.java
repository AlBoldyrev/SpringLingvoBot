package com.vk.lingvobot.application.levels.importDialog;

import com.vk.lingvobot.application.levels.Menu;
import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.menu.MenuLevel;
import com.vk.lingvobot.parser.modelMessageNewParser.ModelMessageNew;
import com.vk.lingvobot.repositories.UserPhraseRepository;
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

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportDialogLevel extends Menu implements IResponseMessageBodyHandler {

    private final ImportDialogService importDialogService;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final UserPhraseRepository userPhraseRepository;

    @Override
    public void handle(User user, ModelMessageNew message) {

        String messageBody = message.getObject().getBody();

        if (messageBody.equals("BACK")) {
            setTheLevel(user, MenuLevel.MAIN.getCode());
            baseMenuAction(user);
        }

        if (message.getObject().getAttachments() == null || message.getObject().getAttachments().size() >1 ) {
            messageService.sendMessageTextOnly(user.getVkId(), "Либо что-то пошло не так, либо пришли мне, пожалуйста всего один файл. Больше не надо... ");
        } else {
            String url = message.getObject().getAttachments().get(0).getDocumnent().getUrl();
            try {
                URL trueURL = new URL(url);
                byte[] bytes = downloadUrl(trueURL);
                importDialogService.importDialog(bytes);
                messageService.sendMessageTextOnly(user.getVkId(), "Большое спасибо. Диалог загружен. ");
                user.setLevel(MenuLevel.MAIN.getCode());
                baseMenuAction(user);
            } catch (MalformedURLException e) {
                System.out.println("Baaaaad url, Sasha. Baaaaaaad url.");
            }
        }
        userRepository.save(user);
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
