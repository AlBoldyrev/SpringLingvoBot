package com.vk.lingvobot.application;

import com.google.gson.JsonObject;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.exceptions.LongPollServerKeyExpiredException;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.LongPollServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
class BotRequestHandler {

    @Autowired
    private MessageNew messageNew;

    private static final int DEFAULT_WAIT = 10;

    private final VkApiClient apiClient;

    private final GroupActor groupActor;
    private final Integer waitTime;


    @Autowired
    BotRequestHandler(VkApiClient client, GroupActor actor) {
        this.apiClient = client;
        this.groupActor = actor;
        this.waitTime = DEFAULT_WAIT;
    }

    void run() {

        Map<String, IResponseHandler> strategyHandlers = new HashMap<>();
        strategyHandlers.put("message_new", messageNew);


        LongPollServer longPollServer = null;
        try {
            longPollServer = apiClient.groupsLongPoll().getLongPollServer(groupActor, groupActor.getGroupId()).execute();
        } catch (ApiException e) {
            log.error("API Exception !!! " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("CLIENT Exception !!! " + e.getStackTrace());
        }
        int ts = Integer.parseInt(Objects.requireNonNull(longPollServer).getTs());


        while (true) try {

            GetLongPollEventsResponse eventsResponse = apiClient.longPoll().getEvents(longPollServer.getServer(),
                    longPollServer.getKey(), ts).waitTime(waitTime).execute();

            for (JsonObject jsonObject : eventsResponse.getUpdates()) {
                String type = jsonObject.get("type").getAsString();
                log.info("jsonType: " + type + "  " + jsonObject);
                IResponseHandler responseHandler = strategyHandlers.get(type);
                try {
                    responseHandler.handle(jsonObject, groupActor);
                } catch (NullPointerException npe) {
                    log.error("This request can not be handled right now." + npe.getStackTrace());
                }

            }

            ts = eventsResponse.getTs();
        } catch (LongPollServerKeyExpiredException | RuntimeException e) {
            try {
                longPollServer = apiClient.groupsLongPoll().getLongPollServer(groupActor, groupActor.getGroupId()).execute();
            } catch (ApiException ex) {
                log.error("API client when trying to connect to LONG POLL server! " + ex.getStackTrace());
            } catch (ClientException ex) {
                log.error("CLIENT client when trying to connect to LONG POLL server! " + ex.getStackTrace());
            }
            log.info(longPollServer.toString());
        } catch (ApiException e) {
            log.error("API client when trying to get LONG POLL server response! " + e.getStackTrace());
        } catch (ClientException e) {
            log.error("CLIENT client when trying to get LONG POLL server response! " + e.getStackTrace());
        }
    }


}
