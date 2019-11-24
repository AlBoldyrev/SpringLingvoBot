package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.PhrasePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhrasePairRepository extends JpaRepository<PhrasePair, Integer> {

    @Query("SELECT pp FROM PhrasePair pp WHERE pp.phrasePairId = :phrasePairId")
    PhrasePair findByPhrasePairId(@Param("phrasePairId") int phrasePairId);

    @Query(value = "SELECT * FROM lingvobot.phrase_pairs ORDER BY phrase_pair_id DESC LIMIT :limit", nativeQuery = true)
    List<PhrasePair> findTopN(@Param("limit") int limit);
}
