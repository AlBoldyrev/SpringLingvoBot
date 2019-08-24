package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.DialogMaxState;
import com.vk.lingvobot.repositories.DialogMaxStateRepository;
import com.vk.lingvobot.services.DialogMaxStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DialogMaxStateServiceImpl implements DialogMaxStateService {

    @Autowired
    DialogMaxStateRepository dialogMaxStateRepository;
    @Override
    public DialogMaxState findById(Integer id) {

        DialogMaxState dialogMaxState = dialogMaxStateRepository.findByDialogMaxStateId(id);
        if (dialogMaxState == null) {
            log.error("There is no dialogMaxState with id: " + id);
            return null;
        }
        return dialogMaxState;
    }
}
