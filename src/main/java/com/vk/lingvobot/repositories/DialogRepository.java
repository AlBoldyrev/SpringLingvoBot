package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    DialogRepository findById(int id);
}
