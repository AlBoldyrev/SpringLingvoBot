package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Phrases")
@Slf4j
public class Phrase {

    @Id
    @GeneratedValue
    @Column(name = "phrase_id")
    private Integer phraseId;

    @Column(name = "phrase_one_side")
    private String phraseOneSide;

    @Column(name = "phrase_other_side")
    private String phraseOtherSide;

    @Column(name = "difficulty")
    private Integer difficulty;
}
