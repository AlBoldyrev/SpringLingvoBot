package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);

    @Query("SELECT u FROM User u WHERE u.vkId = :vkId")
    User findByVkId(@Param("vkId") int vkId);

}
