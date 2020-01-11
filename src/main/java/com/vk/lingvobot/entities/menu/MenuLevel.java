package com.vk.lingvobot.entities.menu;

public enum MenuLevel {
    MAIN("MAIN"),
    DIALOG("DIALOG"),
    PHRASE("PHRASE");

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
