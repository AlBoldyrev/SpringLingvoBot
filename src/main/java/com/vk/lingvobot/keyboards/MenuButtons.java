package com.vk.lingvobot.keyboards;

import lombok.Getter;

public enum MenuButtons {
    PHRASES("Фразы"),
    DIALOGS("Диалоги"),
    NEXT("Далее"),
    BACK("Назад"),
    HOME("В главное меню"),
    EXIT("Выход");

    @Getter
    private String value;

    MenuButtons(String value) {
        this.value = value;
    }

}
