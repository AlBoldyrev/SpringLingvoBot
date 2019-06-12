package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "dialogs")
@Slf4j
public class Dialog implements Serializable {

    @EmbeddedId
    private DialogPK dialogPK;

    @Column(name = "state", insertable=false, updatable=false)
    private Integer state;

    @ManyToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

}


