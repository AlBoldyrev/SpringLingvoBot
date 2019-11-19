package com.vk.lingvobot.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DialogPhrase")
public class DialogPhrase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogPhrase_generator")
    @SequenceGenerator(name="lingvobot_dialogPhrase_generator", sequenceName = "lingvobot_dialogPhrase_sequence")
    @Column(name = "dialog_phrase_id")
    private Integer dialogPhraseId;

    @Column(name = "dialog_phrase_value")
    private String dialogPhraseValue;

    public Integer getDialogPhraseId() {
        return dialogPhraseId;
    }

    public void setDialogPhraseId(Integer dialogPhraseId) {
        this.dialogPhraseId = dialogPhraseId;
    }

    public String getDialogPhraseValue() {
        return dialogPhraseValue;
    }

    public void setDialogPhraseValue(String dialogPhraseValue) {
        this.dialogPhraseValue = dialogPhraseValue;
    }
}
