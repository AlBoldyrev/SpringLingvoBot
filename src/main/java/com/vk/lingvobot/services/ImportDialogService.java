package com.vk.lingvobot.services;

import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import org.springframework.stereotype.Service;

@Service
public interface ImportDialogService {

    void importDialog();
    ImportDialogParser getImportDialogParser();
}
