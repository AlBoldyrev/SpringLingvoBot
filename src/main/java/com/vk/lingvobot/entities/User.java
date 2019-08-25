package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    public User() {}
    public User(Integer userVkId) {
        this.userVkId = userVkId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_user_generator")
    @SequenceGenerator(name="lingvobot_user_generator", sequenceName = "lingvobot_user_sequence")
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_vk_id")
    private Integer userVkId;

    @Column(name = "user_name")
    private String userName;

    @OneToOne
    @JoinColumn(name = "settings_id")
    private Settings settings;

    @OneToMany
    @JoinColumn(name = "user_dialog_id")
    private List<UserDialog> userDialogList;

}
