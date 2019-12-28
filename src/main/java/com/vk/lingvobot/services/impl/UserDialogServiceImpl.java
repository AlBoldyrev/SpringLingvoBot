package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.repositories.DialogMaxStateRepository;
import com.vk.lingvobot.repositories.DialogStateRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.UserDialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class UserDialogServiceImpl implements UserDialogService {

    @Autowired
    private UserDialogRepository userDialogRepository;
    @Autowired
    private DialogStateRepository dialogStateRepository;
    @Autowired
    private MessageServiceKt messageServiceKt;
    @Autowired
    private DialogMaxStateRepository dialogMaxStateRepository;


    @Override
    public UserDialog findById(int id) {
        UserDialog userDialog = userDialogRepository.findById(id);
        if (userDialog == null) {
            log.warn("There is no userDialog with id: " + id);
            return null;
        }
        return userDialog;
    }

    @Override
    public UserDialog findCurrentDialogOfUser(int userId) {
        UserDialog userDialog = userDialogRepository.findCurrentDialogOfUser(userId);
        if (userDialog == null) {
            log.warn("There is no userDialog with userId: " + userId);
            return null;
        }
        return userDialog;
    }

    /**
     * User sends the message - we send particular dialogPhrase and increment state
     */
    public void processCommonDialog(User user, GroupActor groupActor) {

        UserDialog currentUserDialog = findCurrentDialogOfUser(user.getUserId());
        Integer state = currentUserDialog.getState();
        DialogState dialogState = dialogStateRepository.findByDialogIdAndState(currentUserDialog.getDialog().getDialogId(), state);
        DialogPhrase dialogPhrase = dialogState.getDialogPhrase();
        String dialogPhraseValue = dialogPhrase.getDialogPhraseValue();

        messageServiceKt.sendMessageTextOnly(groupActor, user.getVkId(), dialogPhraseValue);
        log.info("Сообщение отправлено! ");

        DialogMaxState dialogMaxState = dialogMaxStateRepository.findByDialogId(currentUserDialog.getDialog().getDialogId());
        Integer dialogMaxStateValue = dialogMaxState.getDialogMaxStateValue();

        if (++state <= dialogMaxStateValue) {
            currentUserDialog.setState(state);
        } else {
            currentUserDialog.setIsFinished(true);
        }
        userDialogRepository.save(currentUserDialog);

    }

    @Transactional
    public Optional<UserDialog> get(Integer id) {
        return userDialogRepository.findById(id);
    }

    @Transactional
    public void create(UserDialog userDialog) {
        userDialogRepository.saveAndFlush(userDialog);
    }


}
