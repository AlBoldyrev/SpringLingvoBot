package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.PhrasePairState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

@Service
public interface PhraseService {

    void processPhrasesPairDialog(User user);
    void createPhrasesPairState(User user);
    boolean checkPhrasePairLastState(Integer phrasePairId);
    void finishPhrasesPairDialog(PhrasePairState phrasePairState, UserDialog currentUserDialog);
    boolean hasUserPhrasesDialogInProcess(User user);


}
