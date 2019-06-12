package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.UserDialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDialogRepository extends JpaRepository<UserDialog, Integer> {

    UserDialog findById(int id);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userVkId = :userVkId AND ud.isFinished = false")
    UserDialog findCurrentDialogOfUser(@Param("userVkId") int userVkId);
}
