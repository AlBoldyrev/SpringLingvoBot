package com.vk.lingvobot.application.strategy;

import com.vk.lingvobot.application.levels.IResponseMessageBodyHandler;
import com.vk.lingvobot.application.levels.MainLevel;
import com.vk.lingvobot.application.levels.dialog.DialogLevelOne;
import com.vk.lingvobot.application.levels.importDialog.ImportDialogLevel;
import com.vk.lingvobot.application.levels.phrase.levelOne.PhraseLevelOne;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoEngRu;
import com.vk.lingvobot.application.levels.phrase.levelTwo.PhraseLevelTwoRuEng;
import com.vk.lingvobot.application.levels.settings.SettingsLevelOne;
import com.vk.lingvobot.application.levels.settings.SettingsLevelTwoDifficulty;
import com.vk.lingvobot.application.levels.settings.SettingsLevelTwoTime;
import com.vk.lingvobot.menu.MenuLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StrategyHandlerService {

    private final MainLevel mainLevel;
    private final DialogLevelOne dialogLevelOne;
    private final PhraseLevelOne phraseLevelOne;
    private final PhraseLevelTwoRuEng phraseLevelTwoRuEng;
    private final PhraseLevelTwoEngRu phraseLevelTwoEngRu;
    private final ImportDialogLevel importDialogLevel;
    private final SettingsLevelOne settingsLevelOne;
    private final SettingsLevelTwoTime settingsLevelTwoTime;
    private final SettingsLevelTwoDifficulty settingsLevelTwoDifficulty;

    @Getter
    private final Map<Integer, IResponseMessageBodyHandler> strategyHandlers = new HashMap<>();

    @PostConstruct
    private void initStrategyHandlers() {
        strategyHandlers.put(MenuLevel.MAIN.getCode(), mainLevel);
        strategyHandlers.put(MenuLevel.DIALOGS.getCode(), dialogLevelOne);
        strategyHandlers.put(MenuLevel.PHRASE.getCode(), phraseLevelOne);
        strategyHandlers.put(MenuLevel.ENG_RU.getCode(), phraseLevelTwoEngRu);
        strategyHandlers.put(MenuLevel.RU_ENG.getCode(), phraseLevelTwoRuEng);
        strategyHandlers.put(MenuLevel.SETTINGS.getCode(), settingsLevelOne);
        strategyHandlers.put(MenuLevel.TIME_SETTINGS.getCode(), settingsLevelTwoTime);
        strategyHandlers.put(MenuLevel.DIFFICULTY_SETTINGS.getCode(), settingsLevelTwoDifficulty);
        strategyHandlers.put(MenuLevel.IMPORT_DIALOG.getCode(), importDialogLevel);
    }
}
