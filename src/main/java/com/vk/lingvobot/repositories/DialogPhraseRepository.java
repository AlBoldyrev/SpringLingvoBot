package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DialogPhraseRepository extends JpaRepository<DialogPhrase, Integer> {

    @Query("SELECT dp FROM DialogPhrase dp WHERE dp.dialogPhraseId = :dialogPhraseId")
    DialogPhrase findByDialogPhraseId(@Param("dialogPhraseId") int dialogPhraseId);
}
