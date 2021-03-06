package com.vk.lingvobot.menu;

public enum MenuLevel {

    MAIN(1),
    DIALOGS(2),
    PHRASE(3),
    ENG_RU(4),
    RU_ENG(5),
    IMPORT_DIALOG(6);


    private final Integer code;

    MenuLevel(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
