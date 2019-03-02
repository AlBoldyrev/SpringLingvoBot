package com.vk.repository;

import com.vk.entities.Phrase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhraseRepository extends JpaRepository<Phrase, Integer> {

    Phrase findByPhraseId(Integer id);
}
