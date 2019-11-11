package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.PhrasePair;
import org.springframework.stereotype.Service;

@Service
public interface PhrasePairService {

    PhrasePair findById(Integer id);
}
