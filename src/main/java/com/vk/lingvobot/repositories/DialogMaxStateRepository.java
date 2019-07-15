package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogMaxState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogMaxStateRepository extends JpaRepository<DialogMaxState, Integer> {

    DialogMaxState findById(int id);

    @Query("SELECT ms from DialogMaxState ms WHERE ms.dialog.dialogId = :dialogId")
    DialogMaxState findByDialogId(@Param("dialogId") int dialogId);
}
