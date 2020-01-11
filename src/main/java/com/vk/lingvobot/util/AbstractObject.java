package com.vk.lingvobot.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class AbstractObject {

    private String type;
    private int firstNumber;
    private int secondNumber;

    @Override
    public String toString() {
        return type + firstNumber + "_" + secondNumber;
    }

}
