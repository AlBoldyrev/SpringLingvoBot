package com.vk.strategy.realizations;

import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.application.IResponseHandler;
import com.vk.entities.Phrase;
import com.vk.repository.PhraseRepository;
import com.vk.strategy.Executor;
import com.vk.strategy.Responsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class WallPostNew implements IResponseHandler {

    @Autowired
    PhraseRepository phraseRepository;

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) {
        System.out.println("wall post");

        Phrase phrase0 = new Phrase();
        phrase0.setPhraseEnglishValue("How can I get to the railway station?");
        phrase0.setPhraseRussianValue("Как мне добраться до железнодорожного вокзала?");

        Phrase phrase1 = new Phrase();
        phrase1.setPhraseEnglishValue("One ticket to London, please.");
        phrase1.setPhraseRussianValue("Один билет до Лондона, пожалуйста.");

        Phrase phrase2 = new Phrase();
        phrase2.setPhraseEnglishValue("I want to reserve two tickets for the 5:30 pm train.");
        phrase2.setPhraseRussianValue("Я хотел бы забронировать два билета на поезд, отходящий в 17.30.");

        phraseRepository.save(phrase0);
        phraseRepository.save(phrase1);
        phraseRepository.save(phrase2);
    }

}
