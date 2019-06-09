package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.UserDialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDialogRepository extends JpaRepository<UserDialog, Integer> {

    UserDialog findById(int id);
}
