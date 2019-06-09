package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Message findById(int id);
}
