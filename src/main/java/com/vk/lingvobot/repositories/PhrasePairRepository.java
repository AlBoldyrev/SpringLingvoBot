package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.PhrasePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhrasePairRepository extends JpaRepository<PhrasePair, Integer> {

    PhrasePair findById(int id);

}
