package com.vk.lingvobot.parser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class Parser {

    @SerializedName("response")
    private List<Response> responses;

}

