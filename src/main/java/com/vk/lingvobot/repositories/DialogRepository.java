package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    @Query("SELECT d FROM Dialog d WHERE d.dialogId = :dialogId and d.state = :stateId")
    Dialog findDialogByDialogId(@Param("dialogId")int dialogId, @Param("stateId") int stateId);

    @Query("SELECT d FROM Dialog d WHERE d.dialogId = 1 and d.state = 1")
    Dialog findStartingDialog();

}
