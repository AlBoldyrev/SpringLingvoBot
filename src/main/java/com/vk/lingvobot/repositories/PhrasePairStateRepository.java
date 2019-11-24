package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.PhrasePairState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhrasePairStateRepository extends JpaRepository<PhrasePairState, Integer> {

    @Query("SELECT pps FROM PhrasePairState pps WHERE pps.phrasePairStateId = :phrasePairStateId")
    PhrasePairState findByPhrasePairStateId(@Param("phrasePairStateId") int phrasePairStateId);

    @Query("SELECT pps FROM PhrasePairState pps WHERE pps.user.userId = :userId")
    PhrasePairState findByUserId(@Param("userId") int userId);

    @Query("SELECT pps FROM PhrasePairState pps WHERE pps.phrasePair.phrasePairId = :phrasePairId")
    PhrasePairState findByPhrasePairId(@Param("phrasePairId") int phrasePairId);
}
