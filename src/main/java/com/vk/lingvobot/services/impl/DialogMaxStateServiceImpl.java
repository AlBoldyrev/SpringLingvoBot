package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.DialogMaxState;
import com.vk.lingvobot.repositories.DialogMaxStateRepository;
import com.vk.lingvobot.services.DialogMaxStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DialogMaxStateServiceImpl implements DialogMaxStateService {

    @Autowired
    private DialogMaxStateRepository dialogMaxStateRepository;

    @Override
    public DialogMaxState findById(int id) {
        DialogMaxState dialogMaxStateById = dialogMaxStateRepository.findById(id);
        if (dialogMaxStateById == null) {
            log.warn("There is no dialogMaxState with id: " + id);
            return null;
        }
        return dialogMaxStateById;
    }
}
