package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.PhrasePairState;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.repositories.PhrasePairStateRepository;
import com.vk.lingvobot.services.PhrasePairStateService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PhrasePairStateServiceImpl implements PhrasePairStateService {

    @Autowired
    PhrasePairStateRepository phrasePairStateRepository;

    private static Map<User, Boolean> usersPhraseStates = new HashMap<>();

    @Override
    public PhrasePairState findById(Integer id) {
        PhrasePairState phrasePairState = phrasePairStateRepository.findByPhrasePairId(id);
        if (phrasePairState == null) {
            log.error("There is no phrasePairState with id: " + id);
            return null;
        }
        return phrasePairState;
    }

    @Override
    public boolean hasUserPhrasesDialogStarted(User user) {
        return usersPhraseStates.containsKey(user);
    }

    @Override
    public  void phrasesDialogStart(User user) {
        usersPhraseStates.put(user, false);
    }

    @Override
    public void phrasesDialogFinish(User user) {
        usersPhraseStates.remove(user);
    }

    @Override
    public boolean checkUserPhraseState(User user) {
        return usersPhraseStates.get(user);
    }

    @Override
    public void changeUserPhrasesState(User user) {
        boolean userPhraseState = usersPhraseStates.get(user);
        usersPhraseStates.put(user, !userPhraseState);
    }
}
