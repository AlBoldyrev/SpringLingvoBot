package com.vk.lingvobot.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    public User() {}
    public User(Integer vkId) {
        this.vkId = vkId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_user_generator")
    @SequenceGenerator(name="lingvobot_user_generator", sequenceName = "lingvobot_user_sequence")
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_vk_id")
    private Integer vkId;

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

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
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
