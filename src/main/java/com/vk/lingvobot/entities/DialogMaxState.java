package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dialog_max_states")
@Slf4j
public class DialogMaxState {

    @Id
    @GeneratedValue
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @ManyToOne
    @JoinColumn(name = "dialogs")
    private Dialog dialog;

    @Column(name = "max_state_value")
    private Integer maxStateValue;
}
