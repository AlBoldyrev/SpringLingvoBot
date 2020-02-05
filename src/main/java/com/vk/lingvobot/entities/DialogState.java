package com.vk.lingvobot.entities;

import com.vk.lingvobot.entities.menu.auxiliary.PostgreSQLEnumType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "DialogState")
@TypeDef(name = "lingvobot.item_state_level", typeClass = PostgreSQLEnumType.class)
public class DialogState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_dialogState_generator")
    @SequenceGenerator(name="lingvobot_dialogState_generator", sequenceName = "lingvobot_dialogState_sequence")
    @Column(name = "dialog_state_id")
    private Integer dialogStateId;

    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;

    @Column(name = "state")
    private Integer state;

    @OneToOne
    @JoinColumn(name = "dialog_phrase_id")
    private DialogPhrase dialogPhrase;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "item_state_level")
    @Type(type = "lingvobot.item_state_level")
    private ItemStateType itemStateType;

}
