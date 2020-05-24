package com.vk.lingvobot.parser.modelMessageNewParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Attachment {

    private String type;
    @SerializedName("doc")
    private Document documnent;
}
