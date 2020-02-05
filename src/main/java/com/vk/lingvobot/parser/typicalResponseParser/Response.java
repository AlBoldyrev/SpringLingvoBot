package com.vk.lingvobot.parser.typicalResponseParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Response {

    @SerializedName("id")
    private int userId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("is_closed")
    private boolean isClosed;

    @SerializedName("can_access_closed")
    private boolean canAccessClosed;

    @SerializedName("domain")
    private String domain;
}
