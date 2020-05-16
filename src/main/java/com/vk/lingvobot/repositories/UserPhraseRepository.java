package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.UserPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPhraseRepository extends JpaRepository<UserPhrase, Integer> {

    @Query("SELECT up FROM UserPhrase up WHERE up.user.userId = :userId AND up.isFinished = false")
    UserPhrase findByUserId(@Param("userId") Integer userId);
}
