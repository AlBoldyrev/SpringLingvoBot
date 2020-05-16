package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_phrases")
@Slf4j
public class UserPhrase {

    @Id
    @GeneratedValue
    @Column(name = "user_phrase_id")
    Integer userPhraseId;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "phrase_id")
    Phrase phrase;

    @Column(name = "is_one_side")
    Boolean isOneSide;

    @Column(name = "is_finished")
    Boolean isFinished = false;


}
