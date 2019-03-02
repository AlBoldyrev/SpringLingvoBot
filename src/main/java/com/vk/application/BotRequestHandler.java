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
import com.vk.strategy.realizations.MessageNew;
import com.vk.strategy.realizations.MessageReply;
import com.vk.strategy.realizations.WallPostNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



@Component
public class BotRequestHandler {


    @Autowired
    MessageNew messageNew;

    @Autowired
    MessageReply messageReply;

    @Autowired
    WallPostNew wallPostNew;

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
    private final Integer waitTime;


    @Autowired
    BotRequestHandler(VkApiClient apiClient, GroupActor groupActor) {
        this.apiClient = apiClient;
        this.groupActor = groupActor;
        this.waitTime = DEFAULT_WAIT;
    }

    void run() throws Exception {

        Map<String, IResponseHandler> strategyHandlers = new HashMap<>();
        strategyHandlers.put("message_new", messageNew);
        strategyHandlers.put("message_reply", messageReply);
        strategyHandlers.put("wall_post_new", wallPostNew);

        GetLongPollServerResponse longPollServer = getLongPollServer();
        int lastTimeStamp = longPollServer.getTs();

        while (true) try {
            GetLongPollEventsResponse eventsResponse = apiClient.longPoll().getEvents(longPollServer.getServer(), longPollServer.getKey(), lastTimeStamp).waitTime(waitTime).execute();
            for (JsonObject jsonObject : eventsResponse.getUpdates()) {
                String type = jsonObject.get("type").getAsString();
                System.out.println("jsonType: " + type + "  " + jsonObject);

                IResponseHandler responseHandler = strategyHandlers.get(type);
                responseHandler.handle(jsonObject, apiClient, groupActor);

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
