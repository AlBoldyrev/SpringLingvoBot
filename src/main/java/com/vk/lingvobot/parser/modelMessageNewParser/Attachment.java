package com.vk.lingvobot.parser.modelMessageNewParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Attachment {

    private String type;
    @SerializedName("doc")
    private Document documnent;
}
