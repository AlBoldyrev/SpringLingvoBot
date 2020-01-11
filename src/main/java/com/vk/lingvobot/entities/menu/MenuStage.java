package com.vk.lingvobot.entities.menu;

import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.menu.auxiliary.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Table(name = "menu_stage")
@Entity
@TypeDef(name = "lingvobot.menu_level", typeClass = PostgreSQLEnumType.class)
public class MenuStage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_menustage_generator")
    @SequenceGenerator(name = "lingvobot_menustage_generator", sequenceName = "lingvobot_menustage_sequence")
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "current_dialog_page")
    Integer currentDialogPage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "menu_level")
    @Type(type = "lingvobot.menu_level")
    MenuLevel menuLevel;
}

