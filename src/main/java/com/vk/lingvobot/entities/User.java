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
    public User(Integer userVkId) {
        this.userVkId = userVkId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_vkid")
    private Integer userVkId;

    @Column(name = "user_name")
    private String userName;

}
