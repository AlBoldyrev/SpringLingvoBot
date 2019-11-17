package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyboardRepository extends JpaRepository<Keyboard, Integer> {

    @Query("SELECT k FROM Keyboard k WHERE k.keyboardId = :keyboardId")
    Keyboard findByKeyboardId(@Param("keyboardId") int keyboardId);
}
