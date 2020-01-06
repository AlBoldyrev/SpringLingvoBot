package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_settings_generator")
    @SequenceGenerator(name="lingvobot_settings_generator", sequenceName = "lingvobot_settings_sequence")
    @Column(name = "settings_id")
    private Integer settingsId;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "user_pronoun")
    private String pronoun = "Вы";

    @Column(name = "difficulty_level")
    private Integer difficultyLevel = 1;

    @Column(name = "lessons_per_day")
    private Integer lessonsPerDay = 5;

    @Column(name = "part_of_the_day")
    private String partOfTheDay = "Вечером";
}
