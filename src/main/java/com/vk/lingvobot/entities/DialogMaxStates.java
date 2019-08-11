package com.vk.lingvobot.entities;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DialogMaxStates")
@Slf4j
public class DialogMaxStates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @OneToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "dialog_max_state_value")
    private Integer dialogMaxStateValue;
}
