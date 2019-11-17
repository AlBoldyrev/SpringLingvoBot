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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogState_generator")
    @SequenceGenerator(name="lingvobot_dialogState_generator", sequenceName = "lingvobot_dialogState_sequence")
    @Column(name = "dialog_state_id")
    private Integer dialogStateId;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "state")
    private Integer state;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "keyboard_id")
    private Keyboard keyboard;

}
