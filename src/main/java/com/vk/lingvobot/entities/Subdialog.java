package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Subdialogs")
@Slf4j
public class Subdialog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_subdialog_generator")
    @SequenceGenerator(name="lingvobot_subdialog_generator", sequenceName = "lingvobot_subdialog_sequence")
    @Column(name = "subdialog_id")
    private Integer dialogId;

    @Column(name = "subdialog_name", unique = true)
    private String dialogName;

}
