package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.entities.menu.MenuLevel;
import com.vk.lingvobot.entities.menu.MenuStage;
import com.vk.lingvobot.keyboard.CustomButton;
import com.vk.lingvobot.keyboards.MenuButtons;
import com.vk.lingvobot.repositories.DialogMaxStateRepository;
import com.vk.lingvobot.repositories.DialogStateRepository;
import com.vk.lingvobot.repositories.MenuStageRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserDialogServiceImpl implements UserDialogService {

    @Autowired
    private UserDialogRepository userDialogRepository;
    @Autowired
    private DialogStateRepository dialogStateRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DialogMaxStateRepository dialogMaxStateRepository;
    @Autowired
    private PhrasePairStateService phrasePairStateService;
    @Autowired
    private PhrasePairService phrasePairService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuStageRepository menuStageRepository;
    @Autowired
    private MessageServiceKt messageServiceKt;

    private List<List<CustomButton>> phraseMenuButtons = new ArrayList<>();
    private List<CustomButton> buttons = new ArrayList<>();

    @PostConstruct
    private void init() {
        buttons.add(new CustomButton(MenuButtons.PHRASES_ENG_RUS.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        buttons.add(new CustomButton(MenuButtons.PHRASES_RUS_ENG.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        buttons.add(new CustomButton(MenuButtons.EXIT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        phraseMenuButtons.add(buttons);
    }

    @Override
    public UserDialog findById(int id) {
        UserDialog userDialog = userDialogRepository.findById(id);
        if (userDialog == null) {
            log.warn("There is no userDialog with id: " + id);
            return null;
        }
        return userDialog;
    }

    @Override
    public UserDialog findCurrentDialogOfUser(int userId) {
        UserDialog userDialog = userDialogRepository.findCurrentDialogOfUser(userId);
        if (userDialog == null) {
            log.warn("There is no userDialog with userId: " + userId);
            return null;
        }
        return userDialog;
    }

    /**
     * User sends the message - we send particular dialogPhrase and increment state
     */
    public void processCommonDialog(User user, GroupActor groupActor) {

        UserDialog currentUserDialog = findCurrentDialogOfUser(user.getUserId());
        Integer state = currentUserDialog.getState();
        DialogState dialogState = dialogStateRepository.findByDialogIdAndState(currentUserDialog.getDialog().getDialogId(), state);
        DialogPhrase dialogPhrase = dialogState.getDialogPhrase();
        String dialogPhraseValue = dialogPhrase.getDialogPhraseValue();
        String dialogPhraseAttach = dialogPhrase.getAttach();

        if (dialogPhraseAttach == null) {
            messageService.sendMessageTextOnly(user.getVkId(), dialogPhraseValue);
        } else {
            messageService.sendMessageWithTextAndAttachments(user.getVkId(), dialogPhraseValue, dialogPhraseAttach);
        }
        log.info("Сообщение отправлено! ");

        DialogMaxState dialogMaxState = dialogMaxStateRepository.findByDialogId(currentUserDialog.getDialog().getDialogId());
        Integer dialogMaxStateValue = dialogMaxState.getDialogMaxStateValue();

        if (++state <= dialogMaxStateValue) {
            currentUserDialog.setState(state);
        } else {
            currentUserDialog.setIsFinished(true);
        }
        userDialogRepository.save(currentUserDialog);
    }

    //Мирослав прости за костыли :c
    public void processPhrasesPairDialog(User user, GroupActor groupActor, String messageBody) {
        MenuStage newStage = menuStageRepository.findByUser(user.getUserId());

        boolean hasUserNoPhrasesDialogStarted = !phrasePairStateService.hasUserPhrasesDialogStarted(user);
        if (hasUserNoPhrasesDialogStarted) {
            phrasePairStateService.phrasesDialogStart(user);
        }

        UserDialog currentUserDialog = findCurrentDialogOfUser(user.getUserId());
        PhrasePairState userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());

        if (userPhrasePairState == null) {
            phrasePairStateService.createPhrasesPairState(user);
            userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());
        }

        if (MenuButtons.EXIT.getValue().equalsIgnoreCase(messageBody)) {
            phrasePairService.finishPhrasesPairDialog(userPhrasePairState, currentUserDialog);
            phrasePairStateService.phrasesDialogFinish(user);
            menuService.handle(user, messageBody, groupActor);
        } else if (MenuButtons.PHRASES_RUS_ENG.getValue().equals(messageBody)) {
            newStage = changeStage(user, MenuLevel.PHRASE_RUS_ENG);
        } else if (MenuButtons.PHRASES_ENG_RUS.getValue().equals(messageBody)) {
            newStage = changeStage(user, MenuLevel.PHRASE_ENG_RUS);
        }

        if (isOnPhraseLevel(user)) {
            messageServiceKt.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(), "Выберите режим обучения: ", phraseMenuButtons);
        } else {
            processPhrase(user, groupActor, userPhrasePairState, currentUserDialog, newStage);
        }

    }

    @Transactional
    public Optional<UserDialog> get(Integer id) {
        return userDialogRepository.findById(id);
    }

    @Transactional
    public void create(UserDialog userDialog) {
        userDialogRepository.saveAndFlush(userDialog);
    }

    private void processPhrase(User user, GroupActor groupActor, PhrasePairState userPhrasePairState,
                               UserDialog currentUserDialog, MenuStage menuStage) {
        if (!phrasePairStateService.checkUserPhraseState(user)) {
            phrasePairService.sendPhraseQuestion(userPhrasePairState, user, null, menuStage, groupActor);
            phrasePairStateService.changeUserPhrasesState(user);
        } else {
            phrasePairService.sendPhraseAnswer(userPhrasePairState, user, menuStage, groupActor);
            phrasePairStateService.changeUserPhrasesState(user);

            Integer currentPhrasePairId = userPhrasePairState.getPhrasePair().getPhrasePairId();

            if (phrasePairService.checkPhrasePairLastState(currentPhrasePairId)) {
                phrasePairService.finishPhrasesPairDialog(userPhrasePairState, currentUserDialog);
                phrasePairStateService.phrasesDialogFinish(user);
            } else {
                userPhrasePairState.getPhrasePair().setPhrasePairId(++currentPhrasePairId);
                phrasePairStateService.save(userPhrasePairState);
                userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());

                String question = "";
                switch (menuStage.getMenuLevel()) {
                    case PHRASE_RUS_ENG:
                        question = "Следующая фраза: \n" + userPhrasePairState.getPhrasePair().getPhraseQuestion();
                        break;
                    case PHRASE_ENG_RUS:
                        question = "Next phrase: \n" + userPhrasePairState.getPhrasePair().getPhraseAnswer();
                }
                phrasePairService.sendPhraseQuestion(userPhrasePairState, user, question, menuStage, groupActor);
                phrasePairStateService.changeUserPhrasesState(user);
            }
        }
    }

    private MenuStage changeStage(User user, MenuLevel menuLevel) {
        MenuStage menuStage = menuStageRepository.findByUser(user.getUserId());
        menuStage.setMenuLevel(menuLevel);
        return menuStageRepository.save(menuStage);
    }

    private boolean isOnPhraseLevel(User user) {
        MenuStage menuStage = menuStageRepository.findByUser(user.getUserId());
        return menuStage.getMenuLevel().equals(MenuLevel.PHRASE);
    }

}
