package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    Dialog findById(int id);

    @Query("SELECT d FROM Dialog d WHERE d.dialogId = 1")
    Dialog findStartingDialog();

}
