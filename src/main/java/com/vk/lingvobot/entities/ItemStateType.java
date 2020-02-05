package com.vk.lingvobot.entities;

import com.vk.lingvobot.entities.menu.MenuLevel;

public enum ItemStateType {

    PHRASE("PHRASE"),
    SUBDIALOG("SUBDIALOG");


    private final String code;

    public ItemStateType fromCode(String code) {
        for (ItemStateType itemStateType: values()) {
            if (itemStateType.code.equals(code)) {
                return itemStateType;
            }
        }
        throw new UnsupportedOperationException("The code " + code + " is not supported!");
    }

    ItemStateType(String code) {
        this.code = code;
    }
}
