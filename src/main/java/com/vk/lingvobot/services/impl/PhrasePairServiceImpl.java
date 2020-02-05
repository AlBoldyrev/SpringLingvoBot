package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.*;
import com.vk.lingvobot.entities.menu.MenuStage;
import com.vk.lingvobot.keyboard.CustomButton;
import com.vk.lingvobot.keyboards.MenuButtons;
import com.vk.lingvobot.repositories.PhrasePairRepository;
import com.vk.lingvobot.repositories.PhrasePairStateRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MessageService;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.PhrasePairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PhrasePairServiceImpl implements PhrasePairService {

    @Autowired
    PhrasePairRepository phrasePairRepository;

    @Autowired
    PhrasePairStateRepository phrasePairStateRepository;

    @Autowired
    UserDialogRepository userDialogRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageServiceKt messageServiceKt;

    private static List<List<CustomButton>> phraseButtonsList = new ArrayList<>();

    static {
        List<CustomButton> customButtons = new ArrayList<>();
        CustomButton customButton = new CustomButton(MenuButtons.EXIT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, "");
        customButtons.add(customButton);
        phraseButtonsList.add(customButtons);
    }

    @Override
    public PhrasePair findById(Integer id) {
        PhrasePair phrasePair = phrasePairRepository.findByPhrasePairId(id);
        if (phrasePair == null) {
            log.error("There is no phrasePair with id: " + id);
            return null;
        }
        return phrasePair;
    }

    @Override
    public void sendPhraseQuestion(PhrasePairState phrasePairState, User user, String question, MenuStage menuStage, GroupActor groupActor) {
        switch (menuStage.getMenuLevel()) {
            case PHRASE_RUS_ENG:
                if (question == null) {
                    question = "Фраза: ";
                    question += phrasePairState.getPhrasePair().getPhraseQuestion();
                }
                break;
            case PHRASE_ENG_RUS:
                if (question == null) {
                    question = "Phrase: ";
                    question += phrasePairState.getPhrasePair().getPhraseAnswer();
                }
                break;
        }
        messageServiceKt.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(), question, phraseButtonsList);
    }

    @Override
    public void sendPhraseAnswer(PhrasePairState phrasePairState, User user, MenuStage menuStage, GroupActor groupActor) {
        String answer = "";
        switch (menuStage.getMenuLevel()) {
            case PHRASE_RUS_ENG:
                answer = "Правильный ответ: \n" + phrasePairState.getPhrasePair().getPhraseAnswer();
                break;
            case PHRASE_ENG_RUS:
                answer = "Correct answer: \n" + phrasePairState.getPhrasePair().getPhraseQuestion();
                break;
        }
        messageServiceKt.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(), answer, phraseButtonsList);
    }

    @Override
    public boolean checkPhrasePairLastState(Integer phrasePairId) {
        List<PhrasePair> phrasePairList = phrasePairRepository.findTopN(1);
        return phrasePairId.equals(phrasePairList.get(0).getPhrasePairId());
    }

    @Override
    public void finishPhrasesPairDialog(PhrasePairState phrasePairState, UserDialog currentUserDialog) {
        phrasePairState.getPhrasePair().setPhrasePairId(1);
        phrasePairStateRepository.save(phrasePairState);
        currentUserDialog.setIsFinished(true);
        userDialogRepository.save(currentUserDialog);
    }

    @Override
    public boolean hasUserPhrasesDialogInProcess(User user) {
        boolean hasUserPhrasesDialogInProcess;
        UserDialog currentUserDialog = userDialogRepository.findCurrentDialogOfUser(user.getUserId());
        if (currentUserDialog == null) {
            return false;
        }
        Dialog dialog = currentUserDialog.getDialog();
        String dialogName = dialog.getDialogName();
        hasUserPhrasesDialogInProcess = dialogName.equalsIgnoreCase("Фразы");
        return hasUserPhrasesDialogInProcess;
    }
}
