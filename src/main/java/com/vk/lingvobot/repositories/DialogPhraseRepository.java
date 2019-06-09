package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogPhraseRepository extends JpaRepository<DialogPhrase, Integer> {

    DialogPhrase findById(int id);
}
