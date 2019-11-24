package com.vk.lingvobot.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phrase_pair_state")
public class PhrasePairState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_phrasePairState_generator")
    @SequenceGenerator(name="lingvobot_phrasePairState_generator", sequenceName = "lingvobot_phrase_pair_state_sequence", allocationSize = 1)
    @Column(name = "phrase_pair_state_id")
    private Integer phrasePairStateId;

    @OneToOne
    @JoinColumn(name = "phrase_pair_id")
    private PhrasePair phrasePair;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
