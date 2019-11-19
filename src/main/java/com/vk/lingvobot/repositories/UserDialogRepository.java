package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.UserDialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDialogRepository extends JpaRepository<UserDialog, Integer> {

    UserDialog findById(int id);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userId = :userId AND ud.isFinished = false AND ud.isCancelled = false")
    UserDialog findCurrentDialogOfUser(@Param("userId") int userId);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userId = :userId and ud.dialog.dialogId = :dialogId")
    UserDialog findUserDialogByUserIdAndDialogId(@Param("userId") int userId, @Param("dialogId") int dialogId);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userId = :userId and ud.dialog.dialogId = :dialogId and ud.isFinished = true")
    UserDialog findFinishedDialogByUserIdAndDialogId(@Param("userId") Integer userId, @Param("dialogId") Integer dialogId);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userId = :userId and ud.dialog.dialogId = :dialogId and ud.isCancelled = true")
    UserDialog findCanceledDialogByUserIdAndDialogId(@Param("userId") Integer userId, @Param("dialogId") Integer dialogId);

    @Query("SELECT ud FROM UserDialog ud WHERE ud.user.userId = :userId and ud.dialog.dialogId = 1")
    UserDialog findUserGreetingDialog(@Param("userId") Integer userId);

}
