package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.DialogMaxState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogMaxStateRepository extends JpaRepository<DialogMaxState, Integer> {

    DialogMaxState findById(int id);
}
