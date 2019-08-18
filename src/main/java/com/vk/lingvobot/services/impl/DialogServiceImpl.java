package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.services.DialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DialogServiceImpl implements DialogService {

    @Autowired
    DialogRepository dialogRepository;

    @Override
    public Dialog findById(Integer id) {
        Dialog dialog = dialogRepository.findByDialogId(id);
        if (dialog == null) {
            log.error("There is no dialog with id: " + id);
            return null;
        }
        return dialog;
    }
}
