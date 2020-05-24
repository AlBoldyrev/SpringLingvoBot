package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    @Query("SELECT d FROM Dialog d WHERE d.dialogId = :dialogId")
    Dialog findByDialogId(@Param("dialogId") int dialogId);

    @Query("SELECT d FROM Dialog d")
    List<Dialog> findAllDialogs();

    @Query("SELECT d FROM Dialog d")
    Page<Dialog> findAllPageDialogs(Pageable pageable);

    @Query("SELECT d FROM Dialog d WHERE d.dialogId <> 1")
    List<Dialog> findAllDialogExceptSettingOne();

    @Query("SELECT d FROM Dialog d WHERE d.dialogId <> 1 AND d.dialogId <> 11")
    Page<Dialog> findAllDialogExceptSettingOne(Pageable pageable);

    @Query("SELECT d FROM Dialog d WHERE d.dialogName = :dialogName")
    Dialog findByDialogName(@Param("dialogName") String dialogName);

}
