package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Keyboards")
@Slf4j
public class Keyboard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_keyboard_generator")
    @SequenceGenerator(name="lingvobot_keyboard_generator", sequenceName = "lingvobot_keyboard_sequence")
    @Column(name = "keyboard_id")
    private Integer keyboardId;

    @Column(name = "keyboard_value")
    private String keyboardValue;
}
