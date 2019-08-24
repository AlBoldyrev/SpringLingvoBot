package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {

    @Query("SELECT s FROM Settings s WHERE s.settingsId = :settingsId")
    Settings findBySettingsId(@Param("settingsId") int settingsId);
}
