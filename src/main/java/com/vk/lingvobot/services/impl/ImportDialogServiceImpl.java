package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.services.ImportDialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImportDialogServiceImpl implements ImportDialogService {

    public void importDialog() {
        System.out.println("Importing...");
    }
}
