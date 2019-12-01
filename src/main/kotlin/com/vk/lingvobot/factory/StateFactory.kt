package com.vk.lingvobot.factory

import com.vk.lingvobot.states.SettingsSetupState
import com.vk.lingvobot.states.impl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StateFactory @Autowired constructor(
    private val pronounSetup: PronounSetup,
    private val difficultySetup: DifficultySetup,
    private val lessonsPerDaySetup: LessonsPerDaySetup,
    private val partOfTheDaySetup: PartOfTheDaySetup,
    private val endSetup: EndSetup,
    private val miscStateSetup: MiscStateSetup
) : AbstractFactory<SettingsSetupState> {

    override fun getInstance(stateIndex: Int): SettingsSetupState {
        return when (stateIndex) {
            1 -> pronounSetup
            5 -> difficultySetup
            6 -> lessonsPerDaySetup
            7 -> partOfTheDaySetup
            8 -> endSetup
            else -> miscStateSetup
        }
    }
}