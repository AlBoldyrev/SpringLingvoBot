package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.keys.PhraseKeys;
import com.vk.lingvobot.repositories.PhrasePairRepository;
import com.vk.lingvobot.repositories.PhrasePairStateRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.PhraseService;
import com.vk.lingvobot.services.UserDialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PhraseServiceImpl implements PhraseService {

    @Autowired
    private UserDialogService userDialogService;

    @Autowired
    private PhrasePairStateRepository phrasePairStateRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PhrasePairRepository phrasePairRepository;

    @Autowired
    private UserDialogRepository userDialogRepository;

    @Override
    public void processPhrasesPairDialog(User user) {

        UserDialog currentUserDialog = userDialogService.findCurrentDialogOfUser(user.getUserId());
        PhrasePairState phrasePairState = phrasePairStateRepository.findByUserId(user.getUserId());

        if (phrasePairState == null) {
            createPhrasesPairState(user);
            phrasePairState = phrasePairStateRepository.findByUserId(user.getUserId());
        }

        String question = phrasePairState.getPhrasePair().getPhraseQuestion();
        messageService.sendMessageTextOnly(user.getVkId(), question);
        Integer phrasePairId = phrasePairState.getPhrasePair().getPhrasePairId();

        if(checkPhrasePairLastState(phrasePairId)) {
            finishPhrasesPairDialog(phrasePairState, currentUserDialog);
        } else {
            phrasePairState.getPhrasePair().setPhrasePairId(++phrasePairId);
            phrasePairStateRepository.save(phrasePairState);
        }
    }

    @Override
    public void createPhrasesPairState(User user) {
        PhrasePair phrasePair = phrasePairRepository.findByPhrasePairId(1);
        PhrasePairState phrasePairState = new PhrasePairState();
        phrasePairState.setPhrasePair(phrasePair);
        phrasePairState.setUser(user);
        phrasePairStateRepository.save(phrasePairState);
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
        currentUserDialog.setIsFinished(true);
        userDialogRepository.save(currentUserDialog);
    }

    @Override
    public boolean hasUserPhrasesDialogInProcess(User user) {
        UserDialog currentUserDialog = userDialogService.findCurrentDialogOfUser(user.getUserId());
        Dialog dialog = currentUserDialog.getDialog();
        String dialogName = dialog.getDialogName();
        return dialogName.equalsIgnoreCase(PhraseKeys.PHRASES_DIALOF_NAME);
    }
}
