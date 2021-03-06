package com.vk.lingvobot.services;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.lingvobot.entities.Phrase;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserPhrase;
import com.vk.lingvobot.keyboards.CustomJavaKeyboard;
import com.vk.lingvobot.repositories.PhraseRepository;
import com.vk.lingvobot.repositories.UserPhraseRepository;
import com.vk.lingvobot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhraseService {

    private final UserRepository userRepository;
    private final UserPhraseRepository userPhraseRepository;
    private final PhraseRepository phraseRepository;
    private final CustomJavaKeyboard customJavaKeyboard;
    private final MessageService messageService;

    public void actionForRuEngTranslation(User user) {

        UserPhrase userPhrase = userPhraseRepository.findByUserId(user.getUserId());
        //TODO костыль с единичкой
        List<Phrase> allPhrasesOfThisDifficulty = phraseRepository.findByDifficulty(1);
        Phrase randomElement = getRandomElement(allPhrasesOfThisDifficulty);

        List<String> back = new ArrayList<>();
        back.add("BACK");
        Keyboard backButton = customJavaKeyboard.createKeyboardWithButtonsBrickByBrick(back);


        if (userPhrase == null) {
            whatToDoWhenUserIsNewToPhrasesInverse(user, randomElement, backButton);
        } else {
            Boolean isOneSide = userPhrase.getIsOneSide();
            if (isOneSide) {
                String phraseOtherSide = userPhrase.getPhrase().getPhraseOtherSide();
                messageService.sendMessageWithTextAndKeyboard(user.getVkId(), phraseOtherSide, backButton);
                userPhrase.setIsOneSide(false);
                userPhraseRepository.save(userPhrase);
            } else {
                userPhrase.setIsFinished(true);
                userPhraseRepository.save(userPhrase);
                whatToDoWhenUserIsNewToPhrasesInverse(user, randomElement, backButton);
            }
        }

    }

    public void actionForEngRuTranslation(User user) {


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
                messageService.sendMessageWithTextAndKeyboard(user.getVkId(), "Правильный ответ: " + phraseOneSide, backButton);
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
        messageService.sendMessageWithTextAndKeyboard(userVkId, "Переведи, плз, фразу: " + phrase.getPhraseOtherSide(), backButton);
    }

    private void whatToDoWhenUserIsNewToPhrasesInverse(User user, Phrase phrase, Keyboard backButton) {
        Integer userVkId = user.getVkId();
        createNewUserPhraseObjectForUser(user, phrase);
        messageService.sendMessageWithTextAndKeyboard(userVkId, "Переведи, плз, фразу: " + phrase.getPhraseOneSide(), backButton);
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

    private <T> T getRandomElement(List<T> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
