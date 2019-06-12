package com.vk.lingvobot.parser.modelMessageNewParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Object {

    private int id;
    private int date;
    private int out;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("read_state")
    private int readState;
    private String title;
    private String body;
    @SerializedName("owner_ids")
    private int[] ownerIds;
}
