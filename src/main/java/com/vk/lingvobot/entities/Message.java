package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Messages")
@Slf4j
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Integer messageId;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message_value")
    private String messageValue;
}
