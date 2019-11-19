package com.vk.lingvobot.entities;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DialogMaxState")
public class DialogMaxState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogMaxState_generator")
    @SequenceGenerator(name="lingvobot_dialogMaxState_generator", sequenceName = "lingvobot_dialogMaxState_sequence")
    @Column(name = "dialog_max_state_id")
    private Integer dialogMaxStateId;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "dialog_max_state_value")
    private Integer dialogMaxStateValue;

    public Integer getDialogMaxStateId() {
        return dialogMaxStateId;
    }

    public void setDialogMaxStateId(Integer dialogMaxStateId) {
        this.dialogMaxStateId = dialogMaxStateId;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Integer getDialogMaxStateValue() {
        return dialogMaxStateValue;
    }

    public void setDialogMaxStateValue(Integer dialogMaxStateValue) {
        this.dialogMaxStateValue = dialogMaxStateValue;
    }
}
