package com.vk.lingvobot.parser.modelMessageNewParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Document {

    private Integer id;
    @SerializedName("owner_id")
    private Integer ownerId;
    private String title;
    private Integer size;
    @SerializedName("ext")
    private String extension;
    private Integer date;
    private Integer type;
    private String url;
    @SerializedName("access_key")
    private String accessKey;
}
