package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dialog_phrases")
@Slf4j
public class DialogPhrase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_generator")
    @SequenceGenerator(name="lingvobot_generator", sequenceName = "lingvobot_sequence")
    @Column(name = "dialog_phrase_id")
    private Integer dialogPhraseId;

    @Column(name = "dialog_phrase_value")
    private String dialogPhraseValue;

}
