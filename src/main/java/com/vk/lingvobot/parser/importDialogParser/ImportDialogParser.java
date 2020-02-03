package com.vk.lingvobot.parser.importDialogParser;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
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
