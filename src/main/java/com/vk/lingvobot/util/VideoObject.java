package com.vk.lingvobot.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class VideoObject implements VKAttachable {

    private String type;
    private int firstNumber;
    private int secondNumber;

    public VideoObject(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        type = VKAttachableObjectTypes.VIDEO.name();
    }
}
