package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.PhrasePair;
import com.vk.lingvobot.repositories.PhrasePairRepository;
import com.vk.lingvobot.services.PhrasePairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PhrasePairServiceImpl implements PhrasePairService {

    @Autowired
    PhrasePairRepository phrasePairRepository;

    @Override
    public PhrasePair findById(Integer id) {
        PhrasePair phrasePair = phrasePairRepository.findByPhrasePairId(id);
        if (phrasePair == null) {
            log.error("There is no phrasePair with id: " + id);
            return null;
        }
        return phrasePair;
    }

}
