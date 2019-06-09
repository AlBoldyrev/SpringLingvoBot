package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dialogs")
@Slf4j
public class Dialog {

    @Id
    @GeneratedValue
    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "dialog_phrases")
    private DialogPhrase dialogPhrase;

}
