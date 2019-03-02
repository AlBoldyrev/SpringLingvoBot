package com.vk.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "phrases")
public class Phrase implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "phrase_id")
    private Integer phraseId;

    @Column(name = "phrase_russian_value")
    private String phraseRussianValue;

    @Column(name = "phrase_english_value")
    private String phraseEnglishValue;

    // ------

    public Integer getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(Integer phraseId) {
        this.phraseId = phraseId;
    }

    public String getPhraseRussianValue() {
        return phraseRussianValue;
    }

    public void setPhraseRussianValue(String phraseRussianValue) {
        this.phraseRussianValue = phraseRussianValue;
    }

    public String getPhraseEnglishValue() {
        return phraseEnglishValue;
    }

    public void setPhraseEnglishValue(String phraseEnglishValue) {
        this.phraseEnglishValue = phraseEnglishValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Phrase that = (Phrase) o;

        return Objects.equals(phraseId, that.phraseId);
    }

    @Override
    public int hashCode() {
        return 31 + (phraseId != null ? phraseId.hashCode() : 0);
    }

}
