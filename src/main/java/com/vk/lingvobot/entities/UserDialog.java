package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_dialogs")
@Slf4j
public class UserDialog {

    @Id
    @GeneratedValue
    @Column(name = "user_dialog_id")
    private Integer userDialogId;

    @ManyToOne
    @JoinColumn(name = "users")
    private User user;

    @ManyToOne
    @JoinColumn(name = "dialogs")
    private Dialog dialog;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    @Column(name = "state")
    private Integer state;

    @Column(name = "is_finished")
    private boolean isFinished;
}
