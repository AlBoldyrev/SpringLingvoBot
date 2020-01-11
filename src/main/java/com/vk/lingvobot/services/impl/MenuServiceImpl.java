package com.vk.lingvobot.services.impl;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.objects.messages.KeyboardButtonActionType;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.User;
import com.vk.lingvobot.entities.UserDialog;
import com.vk.lingvobot.entities.menu.MenuLevel;
import com.vk.lingvobot.entities.menu.MenuStage;
import com.vk.lingvobot.keyboard.CustomButton;
import com.vk.lingvobot.keyboards.MenuButtons;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.MenuStageRepository;
import com.vk.lingvobot.repositories.UserDialogRepository;
import com.vk.lingvobot.services.MenuService;
import com.vk.lingvobot.services.MessageServiceKt;
import com.vk.lingvobot.services.PhrasePairStateService;
import com.vk.lingvobot.services.UserDialogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MessageServiceKt messageService;
    private final MenuStageRepository menuStageRepository;
    private final DialogRepository dialogRepository;
    private final UserDialogRepository userDialogRepository;
    private final UserDialogService userDialogService;
    private final PhrasePairStateService phrasePairStateService;

    private List<List<CustomButton>> mainMenuButtons = new ArrayList<>();
    private List<CustomButton> buttons = new ArrayList<>();

    @PostConstruct
    private void init() {
        buttons.add(new CustomButton(MenuButtons.PHRASES.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        buttons.add(new CustomButton(MenuButtons.DIALOGS.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        mainMenuButtons.add(buttons);
    }

    @Override
    public void processMainDialog(User user, GroupActor groupActor, List<String> buttonList) {

    }

    @Override
    public void handle(User user, String messageBody, GroupActor groupActor) {
        MenuStage menuStage = menuStageRepository.findByUser(user.getUserId());

        if (menuStage == null) {
            menuStage = new MenuStage();
            menuStage.setUser(user);
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStage.setCurrentDialogPage(0);
            menuStage = menuStageRepository.save(menuStage);
        }

        switch (menuStage.getMenuLevel()) {
            case MAIN:
                callMainMenu(user, messageBody, menuStage, groupActor);
                break;
            case DIALOG:
                callDialogMenu(user, messageBody, menuStage, groupActor);
                break;
            case PHRASE:
                callPhraseMenu(user, messageBody, menuStage, groupActor);
                break;
        }
    }

    private void callMainMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        if (messageBody.equals(MenuButtons.DIALOGS.getValue())) {
            menuStage.setMenuLevel(MenuLevel.DIALOG);
            menuStageRepository.save(menuStage);
            callDialogMenu(user, messageBody, menuStage, groupActor);
        } else if (messageBody.equals(MenuButtons.PHRASES.getValue())) {
            menuStage.setMenuLevel(MenuLevel.PHRASE);
            menuStageRepository.save(menuStage);
            callPhraseMenu(user, messageBody, menuStage, groupActor);
        } else {
            messageService.sendMessageWithTextAndKeyboard(
                    groupActor, user.getVkId(), "Выберите режим обучения", mainMenuButtons);
        }
    }

    private void callDialogMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        List<Dialog> allDialogs = dialogRepository.findAllDialogExceptSettingOne();

        if (messageBody.equals(MenuButtons.NEXT.getValue())) {
            int nextDialogPage = menuStage.getCurrentDialogPage() + 1;
            sendDialogsKeyboard(user, nextDialogPage, groupActor);
            menuStage.setCurrentDialogPage(nextDialogPage);
            menuStageRepository.save(menuStage);
        } else if (messageBody.equals(MenuButtons.BACK.getValue())) {
            int previousDialogPage = menuStage.getCurrentDialogPage() - 1;
            sendDialogsKeyboard(user, previousDialogPage, groupActor);
            menuStage.setCurrentDialogPage(previousDialogPage);
            menuStageRepository.save(menuStage);
        } else if (messageBody.equals(MenuButtons.HOME.getValue())) {
            menuStage.setMenuLevel(MenuLevel.MAIN);
            menuStageRepository.save(menuStage);
            callMainMenu(user, messageBody, menuStage, groupActor);
        } else {
            if (allDialogs.stream().anyMatch(dialog -> dialog.getDialogName().equals(messageBody))) {
                enterTheDialog(user, messageBody);
                userDialogService.processCommonDialog(user, groupActor);
            } else {
                sendDialogsKeyboard(user, 0, groupActor);
                menuStage.setCurrentDialogPage(0);
                menuStageRepository.save(menuStage);
            }
        }

    }

    private void callPhraseMenu(User user, String messageBody, MenuStage menuStage, GroupActor groupActor) {
        enterTheDialog(user, messageBody);
        phrasePairStateService.phrasesDialogStart(user);
        userDialogService.processPhrasesPairDialog(user, groupActor, messageBody);
    }

    private void sendDialogsKeyboard(User user, int pageNumber, GroupActor groupActor) {
        List<UserDialog> allUserDialogs = userDialogRepository.findAllUserDialogs(user.getUserId());

        List<List<CustomButton>> allButtons = new ArrayList<>();
        List<CustomButton> buttonsInRow = new ArrayList<>();
        List<CustomButton> navigationButtons = new ArrayList<>();

        Pageable page = PageRequest.of(pageNumber, 5, Sort.by("dialogId").ascending());
        Page<Dialog> dialogsPage = dialogRepository.findAllDialogExceptSettingOne(page);

        if (!dialogsPage.getContent().isEmpty()) {
            dialogsPage.forEach(dialog -> {
                UserDialog foundUserDialog = allUserDialogs.stream().filter(userDialog -> userDialog.getDialog().equals(dialog)).findAny().orElse(null);
                if (foundUserDialog != null && foundUserDialog.getIsFinished()) {
                    buttonsInRow.add(new CustomButton(dialog.getDialogName(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.POSITIVE, ""));
                } else {
                    buttonsInRow.add(new CustomButton(dialog.getDialogName(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.DEFAULT, ""));
                }
            });
            allButtons.add(buttonsInRow);
        }

        if (dialogsPage.isFirst()) {
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            if (dialogsPage.hasNext()) {
                navigationButtons.add(new CustomButton(MenuButtons.NEXT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            }
        } else if (dialogsPage.isLast()) {
            navigationButtons.add(new CustomButton(MenuButtons.BACK.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        } else {
            navigationButtons.add(new CustomButton(MenuButtons.BACK.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.HOME.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
            navigationButtons.add(new CustomButton(MenuButtons.NEXT.getValue(), KeyboardButtonActionType.TEXT, KeyboardButtonColor.PRIMARY, ""));
        }

        allButtons.add(navigationButtons);

        messageService.sendMessageWithTextAndKeyboard(groupActor, user.getVkId(),"Выберите один из диалогов", allButtons);
    }

    /**
     * User sends us name of the particular dialog via Keyboard and we create UserDialog object using this data
     */
    private void enterTheDialog(User user, String message) {
        Dialog dialog = dialogRepository.findByDialogName(message);
        if (dialog == null) {
            log.error("dialog with unexisting name");
        } else {
            UserDialog userDialog = new UserDialog(user, dialog, false, false);
            userDialog.setState(1);
            userDialogService.create(userDialog);
        }
    }
}
