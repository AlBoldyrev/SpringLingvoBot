package com.vk.lingvobot.entities.menu

import com.vk.lingvobot.entities.User
import com.vk.lingvobot.entities.menu.auxiliary.PostgreSQLEnumType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Table(name = "menu_stage")
@Entity
@TypeDef(name = "lingvobot.menu_level", typeClass = PostgreSQLEnumType::class)
data class MenuStage(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lingvobot_menustage_generator")
    @SequenceGenerator(name = "lingvobot_menustage_generator", sequenceName = "lingvobot_menustage_sequence")
    @Column(name = "id")
    var id: Int = 0,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "current_dialog_page")
    var currentDialogPage: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "menu_level")
    @Type(type = "lingvobot.menu_level")
    var menuLevel: MenuLevel
)