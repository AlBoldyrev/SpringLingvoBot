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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="dialog_id", insertable = false, updatable = false),
            @JoinColumn(name="state", insertable = false, updatable = false)
    })
    private Dialog dialog;

    @Column(name = "max_state_value")
    private Integer maxStateValue;
}
