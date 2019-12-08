package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.PhrasePair;
import com.vk.lingvobot.entities.PhrasePairState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

@Service
public interface PhrasePairService {

    PhrasePair findById(Integer id);

    void sendPhraseQuestion(PhrasePairState phrasePairState, User user, String question);

    void sendPhraseAnswer(PhrasePairState phrasePairState, User user);

    boolean checkPhrasePairLastState(Integer phrasePairId);

    void finishPhrasesPairDialog(PhrasePairState phrasePairState, UserDialog currentUserDialog);

    public boolean hasUserPhrasesDialogInProcess(User user);
}
