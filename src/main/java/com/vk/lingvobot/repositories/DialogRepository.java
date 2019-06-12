package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.DialogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, DialogPK> {

    @Query("SELECT d FROM Dialog d WHERE d.dialogPK.dialogId = :dialogId and d.dialogPK.state = :stateId")
    Dialog findDialogViaPrimaryKey(@Param("dialogId")int dialogId, @Param("stateId") int stateId);

    @Query("SELECT d FROM Dialog d WHERE d.dialogPK.dialogId = 1 and d.dialogPK.state = 1")
    Dialog findStartingDialog();

}
