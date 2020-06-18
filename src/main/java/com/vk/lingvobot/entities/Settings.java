package com.vk.lingvobot.entities;

import com.vk.lingvobot.enumeration.DifficultyLevel;
import com.vk.lingvobot.enumeration.PeriodOfTime;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "settings")
@Slf4j
public class Settings {

    @Id
    @GeneratedValue
    @Column(name = "settings_id")
    private Integer settingsId;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @Column(name = "user_pronoun")
    private String pronoun = "Вы";

    @Column(name = "difficulty_level")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel = DifficultyLevel.BEGINNER;

    @Column(name = "lessons_per_day")
    private Integer lessonsPerDay = 5;

    @Column(name = "part_of_the_day")
    @Enumerated(EnumType.STRING)
    private PeriodOfTime partOfTheDay = PeriodOfTime.EVENING;

    @Column(name = "exact_lesson_time")
    private LocalTime exactLessonTime;

    @Column(name = "time_range_start")
    private LocalTime timeRangeStart;

    @Column(name = "time_range_end")
    private LocalTime timeRangeEnd;
}
