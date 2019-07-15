package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "dialogs")
@Slf4j
public class Dialog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_generator")
    @SequenceGenerator(name="lingvobot_generator", sequenceName = "lingvobot_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

}


