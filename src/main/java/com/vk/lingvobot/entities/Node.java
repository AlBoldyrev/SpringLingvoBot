package com.vk.lingvobot.entities;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Nodes")
@Slf4j
public class Node {

    @Id
    @GeneratedValue
    @Column(name = "node_id")
    private Integer nodeId;

    @Column(name = "node_key")
    private Integer nodeKey;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "node_value")
    private String nodeValue;

}
