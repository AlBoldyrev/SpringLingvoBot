package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Data
@Entity
@Table(name = "nodesnext")
@Slf4j
public class NodeNext {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;


    @Column(name = "node_id")
    private Integer nodeId;

    @Column(name = "next_node")
    private Integer nextNode;


    @Column(name = "keyboard_value")
    private String keyboardValue;

}