package com.vk.lingvobot.parser.modelMessageNewParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ModelMessageNew {

    private String type;
    private Object object;
    @SerializedName("group_id")
    private int groupId;
}
