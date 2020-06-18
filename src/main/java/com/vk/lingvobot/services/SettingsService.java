package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.Settings;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.enumeration.DifficultyLevel;
import com.vk.lingvobot.enumeration.PeriodOfTime;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.SettingsRepository;
import com.vk.lingvobot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettingsService {

    private final DialogService dialogService;
    private final UserRepository userRepository;
    private final DialogRepository dialogRepository;
    private final SettingsRepository settingsRepository;

    public Settings findById(Integer id) {
        Settings settings = settingsRepository.findBySettingsId(id);
        if (settings == null) {
            log.error("There is no settings with id: " + id);
            return null;
        }
        return settings;
    }

    /**
     * Create new user using his vkId.
     */
    public User createNewUser(int vkId) {

        log.info("There is no user with vk id: " + vkId + ". Creating new user...");

        User user = new User(vkId);

        Settings settings = new Settings();
        Settings saveSettings = settingsRepository.save(settings);

        user.setSettings(saveSettings);

        //TODO change hardcode
        user.setLevel(2);
        userRepository.save(user);
        Dialog greetingDialog = dialogRepository.findByDialogName("GreetingDialog");
        if (greetingDialog != null) {
            dialogService.proceedTheDialog("GreetingDialog", user, "");
        } else {
            System.out.println("Нет стартового диалога. ");
            user.setLevel(1);
            userRepository.save(user);
        }
        return user;
    }

    public void setBotWriteFrequency(User user, Integer lessonsQuantity) {
        Settings settings = user.getSettings();
        settings.setLessonsPerDay(lessonsQuantity);
        settingsRepository.save(settings);
    }

    public void setDifficultyLevel(User user, DifficultyLevel level) {
        Settings settings = user.getSettings();
        settings.setDifficultyLevel(level);
        settingsRepository.save(settings);
    }

    public void setPartOfTheDay(User user, PeriodOfTime period) {
        Settings settings = user.getSettings();
        settings.setPartOfTheDay(period);
        resetExactLessonTime(settings);
        resetTimeRange(settings);
        settingsRepository.save(settings);
    }

    public void setExactLessonTime(User user, LocalTime exactLessonTime) {
        Settings settings = user.getSettings();
        settings.setExactLessonTime(exactLessonTime);
        resetTimeRange(settings);
        settingsRepository.save(settings);
    }

    public void setLessonTimeRange(User user, LocalTime start, LocalTime end) {
        Settings settings = user.getSettings();
        settings.setTimeRangeStart(start);
        settings.setTimeRangeEnd(end);
        resetExactLessonTime(settings);
        settingsRepository.save(settings);
    }

    private void resetExactLessonTime(Settings settings) {
        settings.setExactLessonTime(null);
    }

    private void resetTimeRange(Settings settings) {
        settings.setTimeRangeStart(null);
        settings.setTimeRangeEnd(null);
    }


}
