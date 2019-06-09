package com.vk.lingvobot.application;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BotRequestHandler botHandler;

    @Autowired
    private GroupActor groupActor;

    @Autowired
    private VkApiClient vk;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {


        try {
            vk.groups().setLongPollSettings(groupActor, groupActor.getGroupId()).enabled(true).messageNew(true).execute();
        } catch (ApiException e) {
            log.error("API Exception happened when application was starting");
        } catch (ClientException e) {
            log.error("CLIENT Exception happened when application was starting");
        }

        botHandler.run();

    }
}
