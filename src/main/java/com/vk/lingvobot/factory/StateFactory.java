package com.vk.lingvobot.factory;

import com.vk.lingvobot.states.SettingsSetupState;
import com.vk.lingvobot.states.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class StateFactory implements AbstractFactory<SettingsSetupState> {

    private final PronounSetup pronounSetup;
    private final DifficultySetup difficultySetup;
    private final LessonsPerDaySetup lessonsPerDaySetup;
    private final PartOfTheDaySetup partOfTheDaySetup;
    private final EndSetup endSetup;
    private final MiscStateSetup miscStateSetup;

    @Override
    public SettingsSetupState getInstance(Integer stateIndex) {
        switch (stateIndex) {
            case 1:
                return pronounSetup;
            case 5:
                return difficultySetup;
            case 6:
                return lessonsPerDaySetup;
            case 7:
                return partOfTheDaySetup;
            case 8:
                return endSetup;
            default:
                return miscStateSetup;
        }
    }
}