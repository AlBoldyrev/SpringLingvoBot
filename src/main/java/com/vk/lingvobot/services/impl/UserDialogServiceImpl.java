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
import com.vk.lingvobot.repositories.DialogRepository;
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
    private DialogRepository dialogRepository;
    @Autowired
    private MenuStageRepository menuStageRepository;
    @Autowired
    private MessageServiceKt messageServiceKt;

    private List<List<CustomButton>> phraseMenuButtons = new ArrayList<>();
    private List<CustomButton> buttons = new ArrayList<>();
    private volatile boolean phraseExitChecker = false;

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
            messageServiceKt.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(),
                    "Выберите режим обучения: ", phraseMenuButtons);
        } else {
            processPhrase(user, groupActor, userPhrasePairState, currentUserDialog, newStage);
            if(this.phraseExitChecker) {
                menuService.handle(user, messageBody, groupActor);
            }
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
                PhrasePair userPhrasePair = getUserPhrasePair(currentPhrasePairId, userPhrasePairState, user,
                        currentUserDialog);
                if (!phrasePairService.checkPhrasePairLastState(userPhrasePair.getPhrasePairId()) &&
                        checkDifficulty(user, userPhrasePair)) {
                    String question = "";
                    switch (menuStage.getMenuLevel()) {
                        case PHRASE_RUS_ENG:
                            question = "Следующая фраза: \n" + userPhrasePair.getPhraseQuestion();
                            break;
                        case PHRASE_ENG_RUS:
                            question = "Next phrase: \n" + userPhrasePair.getPhraseAnswer();
                    }
                    phrasePairService.sendPhraseQuestion(userPhrasePairState, user, question, menuStage, groupActor);
                    phrasePairStateService.changeUserPhrasesState(user);
                }
            }
        }
    }

    private PhrasePair getUserPhrasePair(Integer currentPhrasePairId, PhrasePairState userPhrasePairState, User user,
                                         UserDialog currentUserDialog) {

        userPhrasePairState.getPhrasePair().setPhrasePairId(++currentPhrasePairId);
        phrasePairStateService.save(userPhrasePairState);
        userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());
        PhrasePair currentPhrasePair = userPhrasePairState.getPhrasePair();

        if (checkDifficulty(user, currentPhrasePair)) {
            return currentPhrasePair;
        }
        boolean flag = true;
        while (flag) {
            currentPhrasePairId++;
            userPhrasePairState = phrasePairStateService.findByUserId(user.getUserId());
            currentPhrasePair = userPhrasePairState.getPhrasePair();
            if (!phrasePairService.checkPhrasePairLastState(currentPhrasePairId)) {
                if (checkDifficulty(user, currentPhrasePair)) {
                    currentPhrasePair.setPhrasePairId(currentPhrasePairId);
                    phrasePairStateService.save(userPhrasePairState);
                    flag = false;
                }
            } else {
                phrasePairService.finishPhrasesPairDialog(userPhrasePairState, currentUserDialog);
                phrasePairStateService.phrasesDialogFinish(user);
                flag = false;
                phraseExitChecker = true;
            }
        }
        return currentPhrasePair;
    }

    private boolean checkDifficulty(User user, PhrasePair phrasePair){
        int userDifficultyLevel = user.getSettings().getDifficultyLevel();
        int currentStateDifficulty = phrasePair.getDifficulty();
        return userDifficultyLevel == currentStateDifficulty;
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

    /**
     * User sends us name of the particular dialog via Keyboard and we create UserDialog object using this data
     */
    public void enterTheDialog(User user, String message) {
        Dialog dialog = dialogRepository.findByDialogName(message);
        if (dialog == null) {
            log.error("dialog with unexisting name");
        } else {
            UserDialog userDialog = new UserDialog(user, dialog, false, false, false);
            userDialog.setState(1);
            create(userDialog);
        }
    }


}
