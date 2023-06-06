package org.amaltsev.service.impl;

import lombok.extern.log4j.Log4j;
import org.amaltsev.dao.AppUserDAO;
import org.amaltsev.dao.RawDataDAO;
import org.amaltsev.entity.AppUser;
import org.amaltsev.entity.RawData;
import org.amaltsev.service.MainService;
import org.amaltsev.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.amaltsev.entity.enums.UserState.BASIC_STATE;
import static org.amaltsev.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.amaltsev.service.enums.ServiceCommands.CANCEL;
import static org.amaltsev.service.enums.ServiceCommands.HELP;
import static org.amaltsev.service.enums.ServiceCommands.REGISTRATION;
import static org.amaltsev.service.enums.ServiceCommands.START;

@Service
@Log4j
public class MainServiceImpl implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
    }

    /*
     * попытка найти текущего пользователя(persistenAppUser) в БД и
     * объект представлен в БД, имеет заполненый первичный ключ и связан с сессией hibernate, котороый испоьзует спринг-дата
     * Если пользователя нет, сделать его инициацию AppUser.builder() + (сохранить в БД)
     * */
    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        AppUser persistenAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistenAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                                              .telegramUserId(telegramUser.getId())
                                              .firstName(telegramUser.getFirstName())
                                              .lastName(telegramUser.getLastName())
                                              .userName(telegramUser.getUserName())
                                              //TODO изменить значене isactive по уполчанию после добавления регистрации
                                              .isActive(true)
                                              .state(BASIC_STATE)
                                              .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistenAppUser;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";
        // проверка и обработка входящих команд(пока заглушки)
        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку после установки регистрации
            output = "Пройдите регистрацию ";
        } else {
            log.error("unknown status " + userState);
            output = "Неизвестная ошибка введите /cancel ";
        }
        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        //TODO добавить скачивание сохранения документа
        var answer = "Документ успешно загруженю. Ссылка для скачивания: http://test.ru/get-documents/777";
        sendAnswer(answer, chatId);

    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            var error = "Отменить текущую команду с помощью /cancel для отпарвки файлов";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        //TODO добавить скачивание сохранения документа
        var answer = "Фото успешно загружено. Ссылка для скачивания: http://test.ru/get-photo/777";
        sendAnswer(answer, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        log.debug("process service command");
        if (REGISTRATION.equals(cmd)) {
            return "временно недоступно";
        } else if (HELP.equals(cmd)) {
            return help();
        } else if (START.equals(cmd)) {
            return "Добро пожаловать в Файло-обменный бот.\n Чтобы узнать список команд, введите /help";
        } else {
            return "Добро пожаловать в Файло-обменный бот.\n Чтобы узнать список команд, введите /help";
        }
    }

    private String help() {
        return "Список доступных команд: \n" +
                "/cancel \n" +
                "/registration \n" +
                "/about";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена";
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()     //lombok builder
                                 .event(update)
                                 .build();
        rawDataDAO.save(rawData);               //spring inner method
    }
}
