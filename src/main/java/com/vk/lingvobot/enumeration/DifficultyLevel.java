package com.vk.lingvobot.enumeration;

import lombok.Getter;

public enum DifficultyLevel {
    BEGINNER("Beginner"),
    ELEMENTARY("Elementary"),
    INTERMEDIATE("Intermediate"),
    UPPER_INTERMEDIATE("Upper intermediate"),
    ADVANCED("Advanced"),
    PROFICIENCY("Proficiency");

    @Getter
    private String name;

    DifficultyLevel(String name) {
        this.name = name;
    }
}
