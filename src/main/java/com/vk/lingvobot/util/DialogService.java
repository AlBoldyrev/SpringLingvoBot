package com.vk.lingvobot.util;

import com.vk.lingvobot.entities.Dialog;

public interface DialogService {


    Dialog findDialogViaPrimaryKey(int dialogId,  int stateId);
    Dialog findStartingDialog();

}
