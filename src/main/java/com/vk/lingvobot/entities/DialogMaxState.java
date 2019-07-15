package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "dialog_max_states")
@Slf4j
public class DialogMaxState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_generator")
    @SequenceGenerator(name="lingvobot_generator", sequenceName = "lingvobot_sequence")
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @OneToMany(mappedBy  = "dialog_id")
    @JoinColumn(name="dialog_id")
    private Set<Dialog> dialogs;

    @Column(name = "max_state_value")
    private Integer maxStateValue;
}
