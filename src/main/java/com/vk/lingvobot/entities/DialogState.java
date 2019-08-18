package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "DialogState")
@Slf4j
public class DialogState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialog_state_id")
    private Integer dialogStateId;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "state")
    private Integer state;

    @OneToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

}
