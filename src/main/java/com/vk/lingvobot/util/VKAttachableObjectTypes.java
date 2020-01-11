package com.vk.lingvobot.util;

import java.util.HashMap;
import java.util.Map;

public enum VKAttachableObjectTypes {


    PHOTO("Photo"),
    VIDEO("Video"),
    AUDIO("Audio");

    private final String type;
    private static Map<String, VKAttachableObjectTypes> byType = new HashMap<>();

    static {
        for (VKAttachableObjectTypes type : values()) {
            byType.put(type.type, type);
        }
    }

    VKAttachableObjectTypes(String type) {
        this.type = type;
    }

    public static VKAttachableObjectTypes valueOfType(String type) {
        return byType.get(type);
    }
}
