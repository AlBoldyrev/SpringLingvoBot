package com.vk.strategy.realizations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.entities.User;
import com.vk.model.message_new.ModelMessageNew;
import com.vk.repository.UserRepository;
import com.vk.strategy.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MessageNew implements Executor {

    @Autowired
    UserRepository userRepository;

    private final Random random = new Random();

    public void execute(JsonObject jsonObject, VkApiClient apiClient, GroupActor actor) throws ClientException, ApiException {
        System.out.println("MESSAGE NEW");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ModelMessageNew message = gson.fromJson(jsonObject, ModelMessageNew.class);
        int fromId = message.getInfo().getFromId();
        String messageValue = message.getInfo().getText();
        System.out.println("from_id: " + fromId);
        System.out.println("message: " + messageValue);

        User user = new User();
        user.setUserVkId(fromId);

        userRepository.save(user);


        System.out.println("Message saved.");


//        apiClient.messages().send(actor).message("Hello my friend!").userId(fromId).randomId(random.nextInt()).execute();

    }
}
