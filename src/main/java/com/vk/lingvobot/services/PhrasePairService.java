package com.vk.lingvobot.services;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.lingvobot.entities.PhrasePair;
import com.vk.lingvobot.entities.PhrasePairState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import org.springframework.stereotype.Service;

@Service
public interface PhrasePairService {

    PhrasePair findById(Integer id);

    void sendPhraseQuestion(PhrasePairState phrasePairState, User user, String question, GroupActor groupActor);

    void sendPhraseAnswer(PhrasePairState phrasePairState, User user, GroupActor groupActor);

    boolean checkPhrasePairLastState(Integer phrasePairId);

    void finishPhrasesPairDialog(PhrasePairState phrasePairState, UserDialog currentUserDialog);

    boolean hasUserPhrasesDialogInProcess(User user);
}
