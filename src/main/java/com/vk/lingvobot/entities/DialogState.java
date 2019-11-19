package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "DialogState")
public class DialogState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogState_generator")
    @SequenceGenerator(name="lingvobot_dialogState_generator", sequenceName = "lingvobot_dialogState_sequence")
    @Column(name = "dialog_state_id")
    private Integer dialogStateId;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "state")
    private Integer state;

    @OneToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

    public Integer getDialogStateId() {
        return dialogStateId;
    }

    public void setDialogStateId(Integer dialogStateId) {
        this.dialogStateId = dialogStateId;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public DialogPhrase getDialogPhrase() {
        return dialogPhrase;
    }

    public void setDialogPhrase(DialogPhrase dialogPhrase) {
        this.dialogPhrase = dialogPhrase;
    }
}
