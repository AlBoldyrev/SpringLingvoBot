package com.vk.lingvobot.repositories

import com.vk.lingvobot.menu.MenuStage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MenuStageRepository : JpaRepository<MenuStage, Int> {
    
    @Query("SELECT ms FROM MenuStage ms WHERE ms.user.getUserId = :userId")
    fun findByUser(@Param("userId") userId: Int): MenuStage
    
}