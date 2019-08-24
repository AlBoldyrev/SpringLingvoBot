package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.PhrasePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhrasePairRepository extends JpaRepository<PhrasePair, Integer> {

    @Query("SELECT pp FROM PhrasePair pp WHERE pp.phrasePairId = :phrasePairId")
    PhrasePair findByPhrasePairId(@Param("phrasePairId") int phrasePairId);

}
