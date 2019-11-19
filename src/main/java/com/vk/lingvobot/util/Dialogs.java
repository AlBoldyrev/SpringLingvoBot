package com.vk.lingvobot.util;

import lombok.Getter;

public enum Dialogs {
    GREETING_SET_UP_DIALOG(1);

    @Getter
    private int value;

    Dialogs(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
