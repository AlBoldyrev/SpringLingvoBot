
package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.application.IResponseHandler;
import com.vk.entities.Message;
import com.vk.entities.Phrase;
import com.vk.entities.Storage;
import com.vk.entities.User;
import com.vk.model.message_new.ModelMessageNew;
import com.vk.repository.MessageRepository;
import com.vk.repository.PhraseRepository;
import com.vk.repository.StorageRepository;
import com.vk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class MessageNew implements IResponseHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    StorageRepository storageRepository;

    private final Random random = new Random();

    public void handle(JsonObject jsonObject, VkApiClient apiClient, GroupActor groupActor) throws ClientException, ApiException {
        int min = 1;
        int max = 2;
        Random rnd = new Random(System.currentTimeMillis());
        int number = min + rnd.nextInt(max - min + 1);


        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int fromId = message.getInfo().getFromId();
        String messageValue = message.getInfo().getText();

        User user;
        try {
            user = userRepository.findByUserVkId(fromId);
        } catch (NullPointerException npe) {
            user = new User();
            user.setUserVkId(fromId);
        }

        if (user == null) {
            user = new User ();
            user.setUserVkId(fromId);
        }
        Storage storage = new Storage();
        Phrase phrase = phraseRepository.findByPhraseId(number);
        boolean storageForCheck = true;
        Integer answeredId = 0;
        try {

            //TODO: use lambdas
            List<Storage> storages = storageRepository.findByUserVkId(fromId);
            List<Boolean> answeredList = new ArrayList<>();
            for (Storage storageLocal: storages) {
                Boolean answered = storageLocal.getAnswered();
                answeredId = storageLocal.getStorageId();
                answeredList.add(answered);
            }
            for (Boolean bool :answeredList) {
                if (!bool) {
                    storageForCheck = false;
                    break;
                }
            }
        } catch (NullPointerException npe) {
        }


        if (storageForCheck) {
            apiClient.messages().send(groupActor).message(phrase.getPhraseEnglishValue()).userId(fromId).randomId(random.nextInt()).execute();

            storage.setAnswered(false);
        } else {


            Storage byStorageId = storageRepository.findByStorageId(answeredId);
            Integer phraseId = byStorageId.getPhraseId();
            Phrase byPhraseId = phraseRepository.findByPhraseId(phraseId);
            byStorageId.setAnswered(true);
            storageRepository.save(byStorageId);


            apiClient.messages().send(groupActor).message(byPhraseId.getPhraseRussianValue()).userId(fromId).randomId(random.nextInt()).execute();
            storage.setAnswered(true);
        }



        Message messageForStoring = new Message();
        messageForStoring.setUserVkId(fromId);
        messageForStoring.setMessageValue(messageValue);

        storage.setUserVkId(fromId);
        storage.setPhraseId(phrase.getPhraseId());

        storageRepository.save(storage);
        userRepository.save(user);
        messageRepository.save(messageForStoring);

    }
}

