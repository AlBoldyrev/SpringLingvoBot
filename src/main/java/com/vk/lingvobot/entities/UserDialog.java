package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_dialogs")
@Slf4j
public class UserDialog {

    public UserDialog(){}

    public UserDialog(User user, Dialog dialog, boolean isCancelled, boolean isFinished) {
        this.user = user;
        this.dialog = dialog;
        this.isCancelled = isCancelled;
        this.isFinished = isFinished;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_dialog_id")
    private Integer userDialogId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="dialog_id", referencedColumnName = "dialog_id"),
            @JoinColumn(name="state", referencedColumnName = "state")
    })
    private Dialog dialog;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    @Column(name = "is_finished")
    private boolean isFinished;
}
