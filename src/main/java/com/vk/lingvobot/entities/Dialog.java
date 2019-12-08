package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Dialogs")
@Slf4j
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialog_generator")
    @SequenceGenerator(name="lingvobot_dialog_generator", sequenceName = "lingvobot_dialog_sequence")
    @Column(name = "dialog_id")
    private Integer dialogId;

    @Column(name = "dialog_name", unique = true)
    private String dialogName;

    public Integer getDialogId() {
        return dialogId;
    }

    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    public String getDialogName() {
        return dialogName;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }
}
