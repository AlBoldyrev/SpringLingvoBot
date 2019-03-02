package com.vk.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "storage")
public class Storage implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "storage_id")
    private Integer storageId;

    @Column(name = "user_vkId")
    private Integer userVkId;

    @Column(name = "phrase_id")
    private Integer phraseId;

    @Column(name = "is_answered")
    private Boolean isAnswered;

    // ------

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getUserVkId() {
        return userVkId;
    }

    public void setUserVkId(Integer userVkId) {
        this.userVkId = userVkId;
    }

    public Integer getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(Integer phraseId) {
        this.phraseId = phraseId;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Storage that = (Storage) o;

        return Objects.equals(storageId, that.storageId);
    }

    @Override
    public int hashCode() {
        return 31 + (storageId != null ? storageId.hashCode() : 0);
    }

}