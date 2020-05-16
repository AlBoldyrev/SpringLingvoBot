package com.vk.lingvobot.services;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.entities.Phrase;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserPhrase;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.repositories.PhraseRepository;
import com.vk.lingvobot.repositories.UserPhraseRepository;
import com.vk.lingvobot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class PhraseService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPhraseRepository userPhraseRepository;

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private CustomJavaKeyboard customJavaKeyboard;

    @Autowired
    private MessageService messageService;

    public void actionLevel4(Integer userVkId) {


        User user = userRepository.findByVkId(userVkId);
        UserPhrase userPhrase = userPhraseRepository.findByUserId(user.getUserId());
        //TODO костыль с единичкой
        List<Phrase> allPhrasesOfThisDifficulty = phraseRepository.findByDifficulty(1);
        Phrase randomElement = getRandomElement(allPhrasesOfThisDifficulty);

        List<String> back = new ArrayList<>();
        back.add("BACK");
        Keyboard backButton = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(back);


        if (userPhrase == null) {
            whatToDoWhenUserIsNewToPhrases(user, randomElement, backButton);
        } else {
            Boolean isOneSide = userPhrase.getIsOneSide();
            if (isOneSide) {
                String phraseOtherSide = userPhrase.getPhrase().getPhraseOtherSide();
                messageService.sendMessageWithTextAndKeyboard(userVkId, phraseOtherSide, backButton);
                userPhrase.setIsOneSide(false);
                userPhraseRepository.save(userPhrase);
            } else {
                userPhrase.setIsFinished(true);
                userPhraseRepository.save(userPhrase);
                whatToDoWhenUserIsNewToPhrases(user, randomElement, backButton);
            }
        }

    }



    public void actionLevel5(Integer userVkId) {


        User user = userRepository.findByVkId(userVkId);
        UserPhrase userPhrase = userPhraseRepository.findByUserId(user.getUserId());
        //TODO костыль с единичкой
        List<Phrase> allPhrasesOfThisDifficulty = phraseRepository.findByDifficulty(1);
        Phrase randomElement = getRandomElement(allPhrasesOfThisDifficulty);

        List<String> back = new ArrayList<>();
        back.add("BACK");
        Keyboard backButton = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(back);


        if (userPhrase == null) {
            whatToDoWhenUserIsNewToPhrases(user, randomElement, backButton);
        } else {
            Boolean isOneSide = userPhrase.getIsOneSide();
            if (isOneSide) {
                String phraseOneSide = userPhrase.getPhrase().getPhraseOneSide();
                messageService.sendMessageWithTextAndKeyboard(userVkId, phraseOneSide, backButton);
                userPhrase.setIsOneSide(false);
                userPhraseRepository.save(userPhrase);
            } else {
                userPhrase.setIsFinished(true);
                userPhraseRepository.save(userPhrase);
                whatToDoWhenUserIsNewToPhrases(user, randomElement, backButton);
            }
        }

    }


    private void whatToDoWhenUserIsNewToPhrases(User user, Phrase phrase, Keyboard backButton) {
        Integer userVkId = user.getVkId();
        createNewUserPhraseObjectForUser(user, phrase);
        messageService.sendMessageWithTextAndKeyboard(userVkId, phrase.getPhraseOtherSide(), backButton);
    }
    private UserPhrase createNewUserPhraseObjectForUser(User user, Phrase randomPhrase) {

        UserPhrase userPhrase = new UserPhrase();
        userPhrase.setIsOneSide(true);
        userPhrase.setUser(user);
        userPhrase.setPhrase(randomPhrase);

        userRepository.save(user);
        userPhraseRepository.save(userPhrase);

        return userPhrase;
    }


    private <T> T getRandomElement(List<T> list)
    {
        Random rand = new Random();
        T randomElement = list.get(rand.nextInt(list.size()));
        return randomElement;
    }
}
