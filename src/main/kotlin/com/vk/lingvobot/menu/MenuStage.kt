package com.vk.lingvobot.menu

import com.vk.lingvobot.entities.User
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "menu_stage")
data class MenuStage(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_menustage_generator")
    @SequenceGenerator(name = "lingvobot_menustage_generator", sequenceName = "lingvobot_menustage_sequence")
    @Column(name = "id")
    val id: Int,

    @Column(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "menu_level")
    @Type(type = "lingvobot.menu_level")
    val menuLevel: MenuLevel
)