package com.vk.lingvobot.keyboards;

import lombok.Getter;

public enum MenuButtons {
    PHRASES("Фразы"),
    PHRASES_RUS_ENG(new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA))
            + "->" + new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7))),
    PHRASES_ENG_RUS(new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7))
            + "->" + new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA))),
    DIALOGS("Диалоги"),
    NEXT("Далее"),
    BACK("Назад"),
    HOME("В главное меню"),
    EXIT("Выход"),
    IMPORT_DIALOGS("Загрузить диалог");

    @Getter
    private String value;

    MenuButtons(String value) {
        this.value = value;
    }

}
