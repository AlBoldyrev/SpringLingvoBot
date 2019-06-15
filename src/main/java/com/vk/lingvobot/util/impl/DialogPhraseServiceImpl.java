package com.vk.lingvobot.util.impl;

import com.vk.lingvobot.entities.DialogPhrase;
import com.vk.lingvobot.repositories.DialogPhraseRepository;
import com.vk.lingvobot.util.DialogPhraseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DialogPhraseServiceImpl implements DialogPhraseService {

    @Autowired
    private DialogPhraseRepository dialogPhraseRepository;

    @Override
    public DialogPhrase findById(int id) {
        DialogPhrase dialogPhrase = dialogPhraseRepository.findById(id);
        if (dialogPhrase == null) {
            log.warn("There is no dialogPhrase with id: " + id);
            return null;
        }
        return dialogPhrase;
    }
}
