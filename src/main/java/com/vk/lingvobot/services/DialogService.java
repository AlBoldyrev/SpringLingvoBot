package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Dialog;

public interface DialogService {


    Dialog findDialogViaPrimaryKey(int dialogId,  int stateId);
    Dialog findStartingDialog();

}
