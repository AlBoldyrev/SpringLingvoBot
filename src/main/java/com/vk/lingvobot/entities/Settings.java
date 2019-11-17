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

    @OneToOne(mappedBy = "settings", cascade=CascadeType.ALL)
    private User user;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Column(name = "lessons_per_day")
    private Integer lessonsPerDay;
}
