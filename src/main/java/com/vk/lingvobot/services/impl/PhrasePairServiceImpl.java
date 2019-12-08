package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.repositories.PhrasePairRepository;
import com.vk.lingvobot.repositories.PhrasePairStateRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.PhrasePairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PhrasePairServiceImpl implements PhrasePairService {

    @Autowired
    PhrasePairRepository phrasePairRepository;

    @Autowired
    PhrasePairStateRepository phrasePairStateRepository;

    @Autowired
    UserDialogRepository userDialogRepository;

    @Autowired
    MessageService messageService;

    @Override
    public PhrasePair findById(Integer id) {
        PhrasePair phrasePair = phrasePairRepository.findByPhrasePairId(id);
        if (phrasePair == null) {
            log.error("There is no phrasePair with id: " + id);
            return null;
        }
        return phrasePair;
    }

    @Override
    public void sendPhraseQuestion(PhrasePairState phrasePairState, User user, String question) {
        if(question == null) {
            question = phrasePairState.getPhrasePair().getPhraseQuestion();
        }
        messageService.sendMessageTextOnly(user.getVkId(), question);
    }

    @Override
    public void sendPhraseAnswer(PhrasePairState phrasePairState, User user) {
        String answer = "Правильный ответ: \n" + phrasePairState.getPhrasePair().getPhraseAnswer();
        messageService.sendMessageTextOnly(user.getVkId(), answer);
    }

    @Override
    public boolean checkPhrasePairLastState(Integer phrasePairId) {
        List<PhrasePair> phrasePairList = phrasePairRepository.findTopN(1);
        return phrasePairId.equals(phrasePairList.get(0).getPhrasePairId());
    }

    @Override
    public void finishPhrasesPairDialog(PhrasePairState phrasePairState, UserDialog currentUserDialog) {
        phrasePairState.getPhrasePair().setPhrasePairId(1);
        phrasePairStateRepository.save(phrasePairState);
        currentUserDialog.setFinished(true);
        userDialogRepository.save(currentUserDialog);
    }

    @Override
    public boolean hasUserPhrasesDialogInProcess(User user) {
        UserDialog currentUserDialog = userDialogRepository.findCurrentDialogOfUser(user.getUserId());
        Dialog dialog = currentUserDialog.getDialog();
        String dialogName = dialog.getDialogName();
        return dialogName.equalsIgnoreCase("Фразы");
    }
}
