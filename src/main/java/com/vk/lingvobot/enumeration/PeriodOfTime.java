package com.vk.lingvobot.enumeration;

import lombok.Getter;

public enum PeriodOfTime {
    MORNING("Утро"),
    AFTERNOON("День"),
    EVENING("Вечер"),
    NIGHT("Ночь");

    @Getter
    private String name;

    PeriodOfTime(String name) {
        this.name = name;
    }
}
