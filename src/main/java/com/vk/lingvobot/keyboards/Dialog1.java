package com.vk.lingvobot.keyboards;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public final class Dialog1 {

    public Map<Integer, String> stateAndKeyboard = new HashMap<>();

    public static final String KEYBOARD1 = "{\"one_time\": true, \"buttons\":[[{\"action\":{\"type\":\"text\",\"payload\"" +
            ":\"{\\\"button\\\": \\\"3\\\"}\",\"label\":\"На \\\"Ты\\\"!\"},\"color\":\"positive\"},{\"action\":{\"type\"" +
            ":\"text\",\"payload\":\"{\\\"button\\\": \\\"1\\\"}\",\"label\":\"На \\\"Вы\\\"\"},\"color\":\"positive\"}]]}";

    public static final String KEYBOARD2 = "{\"one_time\": true, \"buttons\":[[{\"action\":{\"type\":\"text\",\"payload\":\"" +
            "{\\\"button\\\": \\\"3\\\"}\",\"label\":\"Okey :)\"},\"color\":\"positive\"}]]}";
    {
        stateAndKeyboard.put(1,KEYBOARD1);
        stateAndKeyboard.put(2, KEYBOARD2);
    }

    public Map<Integer, String> getStateAndKeyboard() {
        return stateAndKeyboard;
    }
}
