package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.DialogPhrase;
import org.springframework.stereotype.Service;

@Service
public interface DialogPhraseService {

    DialogPhrase findById(Integer id);
}
