package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.repositories.DialogMaxStateRepository;
import com.vk.lingvobot.repositories.DialogStateRepository;
import com.vk.lingvobot.repositories.PhrasePairStateRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.PhrasePairService;
import com.vk.lingvobot.services.PhrasePairStateService;
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
    @Autowired
    private PhrasePairStateService phrasePairStateService;
    @Autowired
    private PhrasePairService phrasePairService;


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
            currentUserDialog.setFinished(true);
        }
        userDialogRepository.save(currentUserDialog);

    }

    public void processPhrasesPairDialog(User user) {

        UserDialog currentUserDialog = findCurrentDialogOfUser(user.getUserId());
        PhrasePairState userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());

        if (userPhrasePairState == null) {
            phrasePairStateService.createPhrasesPairState(user);
            userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());
        }

        if(!phrasePairStateService.checkUserPhraseState(user)) {
            phrasePairService.sendPhraseQuestion(userPhrasePairState, user, null);
            phrasePairStateService.changeUserPhrasesState(user);

        } else {
            phrasePairService.sendPhraseAnswer(userPhrasePairState, user);
            phrasePairStateService.changeUserPhrasesState(user);

            Integer currentPhrasePairId = userPhrasePairState.getPhrasePair().getPhrasePairId();

            if(phrasePairService.checkPhrasePairLastState(currentPhrasePairId)) {
                phrasePairService.finishPhrasesPairDialog(userPhrasePairState, currentUserDialog);
                phrasePairStateService.phrasesDialogFinish(user);
            } else {
                userPhrasePairState.getPhrasePair().setPhrasePairId(++currentPhrasePairId);
                phrasePairStateService.save(userPhrasePairState);
                userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());

                String question = "Следующая фраза: \n" + userPhrasePairState.getPhrasePair().getPhraseQuestion();
                phrasePairService.sendPhraseQuestion(userPhrasePairState, user, question);
                phrasePairStateService.changeUserPhrasesState(user);
            }
        }
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
