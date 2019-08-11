package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Integer settingId;

    @OneToOne(mappedBy = "settings")
    private User user;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Column(name = "lessons_per_day")
    private Integer lessonsPerDay;
}
