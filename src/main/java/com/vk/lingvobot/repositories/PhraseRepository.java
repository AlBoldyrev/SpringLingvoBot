package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhraseRepository extends JpaRepository<Phrase, Integer> {

    @Query("SELECT p FROM Phrase p WHERE p.phraseOneSide = :phraseOneSide")
    Phrase findByPhraseOneSide(@Param("phraseOneSide") String phraseOneSide);

    @Query("SELECT p FROM Phrase p WHERE p.phraseOtherSide = :phraseOtherSide")
    Phrase findByPhraseOtherSide(@Param("phraseOtherSide") String phraseOtherSide);

    @Query("SELECT p FROM Phrase p WHERE p.difficulty = :difficulty")
    List<Phrase> findByDifficulty(@Param("difficulty") Integer difficulty);




}
