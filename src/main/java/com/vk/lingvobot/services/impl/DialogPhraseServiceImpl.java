package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.DialogPhrase;
import com.vk.lingvobot.repositories.DialogPhraseRepository;
import com.vk.lingvobot.services.DialogPhraseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DialogPhraseServiceImpl implements DialogPhraseService {

    @Autowired
    DialogPhraseRepository dialogPhraseRepository;

    @Override
    public DialogPhrase findById(Integer id) {
        DialogPhrase dialogPhrase = dialogPhraseRepository.findByDialogPhraseId(id);
        if (dialogPhrase == null) {
            log.error("There is no dialogPhrase with id: " + id);
            return null;
        }
        return dialogPhrase;
    }
}
