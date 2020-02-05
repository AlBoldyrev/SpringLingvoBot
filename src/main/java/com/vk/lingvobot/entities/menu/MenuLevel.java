package com.vk.lingvobot.entities.menu;

public enum MenuLevel {
    MAIN("MAIN"),
    DIALOG("DIALOG"),
    PHRASE("PHRASE"),
    PHRASE_RUS_ENG("PHRASE_RUS_ENG"),
    PHRASE_ENG_RUS("PHRASE_ENG_RUS"),
    IMPORT_DIALOG("IMPORT_DIALOG");

    private final String code;

    public MenuLevel fromCode(String code) {
        for (MenuLevel decisionType: values()) {
            if (decisionType.code.equals(code)) {
                return decisionType;
            }
        }
        throw new UnsupportedOperationException("The code " + code + " is not supported!");
    }

    MenuLevel(String code) {
        this.code = code;
    }
}
