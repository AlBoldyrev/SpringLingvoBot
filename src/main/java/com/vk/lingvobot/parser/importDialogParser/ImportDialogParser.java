package com.vk.lingvobot.parser.importDialogParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ImportDialogParser {

    @SerializedName("class")
    private String clazz;

    private String linkFromPortIdProperty;

    private String linkToPortIdProperty;

    private ModelData modelData;

    @SerializedName("nodeDataArray")
    private List<NodeData> nodeDataList;

    @SerializedName("linkDataArray")
    private List<LinkData> linkDataList;



}
