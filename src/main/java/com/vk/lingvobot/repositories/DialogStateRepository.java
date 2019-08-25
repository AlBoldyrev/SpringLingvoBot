package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DialogStateRepository extends JpaRepository<DialogState, Integer> {

    @Query("SELECT dts FROM DialogState dts WHERE dts.dialogStateId = :dialogStateId")
    DialogState findByDialogStateId(@Param("dialogStateId") int dialogStateId);

    @Query("SELECT dts FROM DialogState dts WHERE dts.dialog.dialogId = :dialogId and dts.state = :state")
    DialogState findByDialogIdAndState(@Param("dialogId") int dialogId, @Param("state") int state);

    @Query("SELECT dts FROM DialogState dts WHERE dts.dialog.dialogId = :dialogId")
    List<DialogState> findAllByDialogId(@Param("dialogId") int dialogId);

}
