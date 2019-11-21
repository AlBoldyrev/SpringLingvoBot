package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DialogService {

    Dialog findById(Integer id);
    List<Dialog> getAllDialogs();
    Dialog getDialogViaName(String name);
}
