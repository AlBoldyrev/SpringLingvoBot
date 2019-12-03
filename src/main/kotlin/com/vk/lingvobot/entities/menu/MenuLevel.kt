package com.vk.lingvobot.entities.menu

enum class MenuLevel {
    MAIN("MAIN"),
    DIALOG("DIALOG"),
    PHRASE("PHRASE");

    private val code: String

    constructor(code: String) {
        this.code = code
    }

    open fun fromCode(code: String): MenuLevel? {
        for (decisionType in values()) {
            if (decisionType.code == code) {
                return decisionType
            }
        }
        throw UnsupportedOperationException("The code $code is not supported!")
    }
}