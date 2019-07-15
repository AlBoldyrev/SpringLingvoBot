package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phrase_pairs")
@Slf4j
public class PhrasePair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_generator")
    @SequenceGenerator(name="lingvobot_generator", sequenceName = "lingvobot_sequence")
    @Column(name = "phrase_pair_id")
    private Integer phrasePairId;

    @Column(name = "phrase_question")
    private String phraseQuestion;

    @Column(name = "phrase_answer")
    private String phraseAnswer;
}
