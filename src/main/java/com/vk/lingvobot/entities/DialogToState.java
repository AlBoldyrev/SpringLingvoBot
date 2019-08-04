package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "DialogMaxStates")
@Slf4j
public class DialogToState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialog_state_id")
    private Integer dialogStateId;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private List<Dialog> dialogs;

    @Column(name = "state")
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

}
