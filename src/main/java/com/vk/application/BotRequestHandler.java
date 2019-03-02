package com.vk.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.exceptions.LongPollServerKeyExpiredException;
import com.vk.api.sdk.objects.groups.responses.GetLongPollServerResponse;
import com.vk.entities.Message;
import com.vk.entities.Phrase;
import com.vk.entities.Storage;
import com.vk.entities.User;
import com.vk.model.message_new.ModelMessageNew;
import com.vk.repository.MessageRepository;
import com.vk.repository.PhraseRepository;
import com.vk.repository.StorageRepository;
import com.vk.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



@Component
public class BotRequestHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    StorageRepository storageRepository;


    private static final Logger LOG = LoggerFactory.getLogger(BotRequestHandler.class);
    private static final int DEFAULT_WAIT = 10;

    private final VkApiClient apiClient;

    private final GroupActor groupActor;
    private final Random random = new Random();
    private UserActor userActor;
    private final Integer groupId;
    private final Integer waitTime;

    @Autowired
    BotRequestHandler(VkApiClient apiClient, GroupActor groupActor) {
        this.apiClient = apiClient;
        this.groupActor = groupActor;
        this.groupId = groupActor.getGroupId();
        this.waitTime = DEFAULT_WAIT;
    }

    void handle(int userId) {
        try {
            apiClient.messages().send(groupActor).message("Hello my friend!").userId(userId).randomId(random.nextInt()).execute();
        } catch (ApiException e) {
            LOG.error("INVALID REQUEST", e);
        } catch (ClientException e) {
            LOG.error("NETWORK ERROR", e);
        }
    }
    void run() throws Exception {

        GetLongPollServerResponse longPollServer = getLongPollServer();
        int lastTimeStamp = longPollServer.getTs();
        while (true) try {
            GetLongPollEventsResponse eventsResponse = apiClient.longPoll().getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp).waitTime(waitTime).execute();
            for (JsonObject jsonObject : eventsResponse.getUpdates()) {
                String type = jsonObject.get("type").getAsString();
                System.out.println("jsonType: " + type + "  " + jsonObject);

                if (type.equals("message_new")) {

                    int min = 1;
                    int max = 2;
                    Random rnd = new Random(System.currentTimeMillis());
                    int number = min + rnd.nextInt(max - min + 1);


                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
                    int fromId = message.getInfo().getFromId();
                    String messageValue = message.getInfo().getText();
                    System.out.println("from_id: " + fromId);
                    System.out.println("message: " + messageValue);
                    User user = null;
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

                    userRepository.save(user);
                    messageRepository.save(messageForStoring);



                    storage.setUserVkId(fromId);
                    storage.setPhraseId(phrase.getPhraseId());
                    storageRepository.save(storage);



                }

                if (type.equals("message_reply")) {

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
                    int fromId = message.getInfo().getFromId();
                    String messageValue = message.getInfo().getText();

//                    apiClient.messages().send(groupActor).message("Hello my friend!").userId(fromId).randomId(random.nextInt()).execute();
                }

                if (type.equals("wall_post_new")) {
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

//                    ActionType.valueOf(type).execute(jsonObject, apiClient, groupActor);

            }
            lastTimeStamp = eventsResponse.getTs();
        } catch (LongPollServerKeyExpiredException e) {
            longPollServer = getLongPollServer();
            LOG.info(longPollServer.toString());
        }
    }

    private GetLongPollServerResponse getLongPollServer() throws ClientException, ApiException {
        if (groupActor != null) {
            return apiClient.groups().getLongPollServer(groupActor).execute();
        }

        return apiClient.groups().getLongPollServer(userActor, groupActor.getGroupId()).execute();
    }
}
