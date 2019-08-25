package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogMaxState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DialogMaxStateRepository extends JpaRepository<DialogMaxState, Integer> {

    @Query("SELECT dms FROM DialogMaxState dms WHERE dms.dialogMaxStateId = :dialogMaxStateId")
    DialogMaxState findByDialogMaxStateId(@Param("dialogMaxStateId") int dialogMaxStateId);

    @Query("SELECT dms FROM DialogMaxState dms WHERE dms.dialog.dialogId = :dialogId")
    DialogMaxState findByDialogId(@Param("dialogId") int dialogId);

}
