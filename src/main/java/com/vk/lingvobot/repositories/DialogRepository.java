package com.vk.lingvobot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<DialogRepository, Integer> {

    DialogRepository findById(int id);
}
