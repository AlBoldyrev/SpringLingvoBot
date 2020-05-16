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
    @GeneratedValue
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_vk_id")
    private Integer vkId;

    @Column(name = "user_name")
    private String userName;

    @OneToOne
    @JoinColumn(name = "settings_id")
    private Settings settings;

    @Column(name = "level")
    private Integer level;

}
