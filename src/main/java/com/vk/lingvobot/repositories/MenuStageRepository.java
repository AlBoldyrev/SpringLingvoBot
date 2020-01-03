package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.menu.MenuStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuStageRepository extends JpaRepository<MenuStage, Integer> {

    @Query("SELECT ms FROM MenuStage ms WHERE ms.user.id = :userId")
    MenuStage findByUser(@Param("userId")Integer userId);

}
