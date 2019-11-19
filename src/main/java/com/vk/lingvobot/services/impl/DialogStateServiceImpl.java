package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.repositories.DialogStateRepository;
import com.vk.lingvobot.services.DialogStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DialogStateServiceImpl implements DialogStateService {

    @Autowired
    private DialogStateRepository dialogStateRepository;

    @Override
    public DialogState findById(Integer id) {
        DialogState dialogState = dialogStateRepository.findByDialogStateId(id);
        if (dialogState == null) {
            log.error("There is no dialogState with id: " + id);
            return null;
        }
        return dialogState;
    }
}
