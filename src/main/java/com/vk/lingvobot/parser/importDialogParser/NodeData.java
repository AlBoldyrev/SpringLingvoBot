package com.vk.lingvobot.parser.importDialogParser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Data
@Slf4j
public class NodeData implements Comparable {

    private String text;
    private String figure;
    private String fill;
    private int key;
    private String loc;
    private String size;

    @Override
    public int compareTo(@NotNull Object o) {
        int compareLoc = convertLocationToIntegerValue(((NodeData) o).getLoc());
        log.info("Object with location " + convertLocationToIntegerValue(this.getLoc()) + "is bigger than object with location " + compareLoc);
        return convertLocationToIntegerValue(this.getLoc()) - compareLoc;

    }

    public int convertLocationToIntegerValue(String loc) {
        String[] locations = loc.split(" ");
        String ourLocWeFindString = locations[1];
        return Integer.parseInt(ourLocWeFindString);

    }
}
