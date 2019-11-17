package com.vk.lingvobot.entities;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DialogMaxState")
@Slf4j
public class DialogMaxState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogMaxState_generator")
    @SequenceGenerator(name="lingvobot_dialogMaxState_generator", sequenceName = "lingvobot_dialogMaxState_sequence")
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "dialog_max_state_value")
    private Integer dialogMaxStateValue;
}
