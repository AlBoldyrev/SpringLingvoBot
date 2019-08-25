package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Dialogs")
@Slf4j
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialog_generator")
    @SequenceGenerator(name="lingvobot_dialog_generator", sequenceName = "lingvobot_dialog_sequence")
    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "dialog_name")
    private String dialogName;
}
