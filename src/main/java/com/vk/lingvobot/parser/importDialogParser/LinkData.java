package com.vk.lingvobot.parser.importDialogParser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkData {

    private int from;
    private int to;
    private String fromPort;
    private String toPort;

    public LinkData(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkData linkData = (LinkData) o;
        return from == linkData.from &&
                to == linkData.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
