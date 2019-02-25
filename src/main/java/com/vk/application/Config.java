package com.vk.application;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class Config {

    private static final String PROPERTIES_FILE = "application.properties";

    @Bean
    public HttpTransportClient httpClient() {
        System.out.println("------------------bean httpClient is initializated!");
        return HttpTransportClient.getInstance();
    }

    @Bean
    public VkApiClient vkApiClient(HttpTransportClient httpClient) {
        System.out.println("------------------bean vkApiClient is initializated!");
        return new VkApiClient(httpClient);
    }

    @Bean
    public GroupActor groupActor(Properties properties) {
        System.out.println("------------------bean groupActor is initializated!");
        return createGroupActor(properties);
    }

    @Bean
    public Properties properties() throws IOException {
        System.out.println("------------------bean properties is initializated!");
        return readProperties();
    }



    private static GroupActor createGroupActor(Properties properties) {
        String groupId = properties.getProperty("groupId");
        String accessToken = properties.getProperty("token");
        return new GroupActor(Integer.parseInt(groupId), accessToken);
    }

    private static Properties readProperties() throws IOException {
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + PROPERTIES_FILE + "' not found in the classpath");
        }
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        } finally {
            inputStream.close();
        }
    }
}
