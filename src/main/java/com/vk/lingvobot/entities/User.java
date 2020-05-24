package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@Slf4j
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

    @Column(name = "page")
    private Integer page;

}
