package com.vk.lingvobot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

@Slf4j
@Component
public class Activity {

    public int howMuchShouldActivityMessageBeDisplayed (String message) {

        int howMuchShouldActivityMessageBeDisplayed;

        int length = message.toCharArray().length;

        log.debug("length of message is: " + length + " symbols.");

        if (length < 50) howMuchShouldActivityMessageBeDisplayed = 2;
        else if (length < 200) howMuchShouldActivityMessageBeDisplayed = 4;
        else if (length < 500) howMuchShouldActivityMessageBeDisplayed = 7;
        else if (length < 1000) howMuchShouldActivityMessageBeDisplayed = 14;
        else howMuchShouldActivityMessageBeDisplayed = 20;

        return howMuchShouldActivityMessageBeDisplayed;
    }
}
