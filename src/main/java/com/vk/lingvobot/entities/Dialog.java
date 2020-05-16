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
    @GeneratedValue
    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "dialog_name", unique = true)
    private String dialogName;
}
