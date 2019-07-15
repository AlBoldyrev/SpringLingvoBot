package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.services.DialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DialogServiceImpl implements DialogService {

    @Autowired
    private DialogRepository dialogRepository;

    @Override
    public Dialog findDialogViaPrimaryKey(int dialogId, int stateId) {
        Dialog dialog = dialogRepository.findDialogByDialogId(dialogId, stateId);
        if (dialog == null) {
            log.warn("There is no dialog with dialogId: " + dialogId + " and stateId: " + stateId);
            return null;
        }
        return dialog;
    }

    @Override
    public Dialog findStartingDialog() {
        Dialog dialog = dialogRepository.findStartingDialog();
        if (dialog == null) {
            log.warn("There is no starting dialog!");
            return null;
        }
        return dialog;
    }
}
