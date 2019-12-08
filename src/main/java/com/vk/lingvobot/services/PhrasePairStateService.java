package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.PhrasePairState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

@Service
public interface PhrasePairStateService {

    PhrasePairState findById(Integer id);

    boolean checkUserPhraseState(User user);

    boolean hasUserPhrasesDialogStarted(User user);

    void phrasesDialogStart(User user);

    void phrasesDialogFinish(User user);

    void changeUserPhrasesState(User user);

    void createPhrasesPairState(User user);

    PhrasePairState findByUserId(Integer id);

    void save(PhrasePairState phrasePairState);
}
