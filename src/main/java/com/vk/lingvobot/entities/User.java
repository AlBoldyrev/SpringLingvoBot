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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserVkId() {
        return userVkId;
    }

    public void setUserVkId(Integer userVkId) {
        this.userVkId = userVkId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

}
