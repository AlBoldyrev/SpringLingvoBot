package com.vk.lingvobot.services.impl;

import com.vk.lingvobot.entities.PhrasePair;
import com.vk.lingvobot.repositories.PhrasePairRepository;
import com.vk.lingvobot.services.PhrasePairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PhrasePairServiceImpl implements PhrasePairService {

    @Autowired
    private PhrasePairRepository phrasePairRepository;


    @Override
    public PhrasePair findById(int id) {
        PhrasePair phrasePair = phrasePairRepository.findById(id);
        if (phrasePair == null) {
            log.warn("There is no PhrasePair with id: " + id);
            return null;
        }
        return phrasePair;
    }
}
