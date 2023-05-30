package org.amaltsev.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;


@Component              //Spring сможет создать бин и поместить его в свой контекст
@Log4j                  //Lombok for hidden Logger creation
public class TelegramBot extends TelegramLongPollingBot {

    //    Implement methods from TelegramLongPollingBot
    //    Class with telegram server interactions
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;


    //в телеграм бот внедряется ссылка на updateController после внедрения зависимости, выполняется init() метод, через который передаем
    // ссылку на сам телеграмбот внутрь updateController
    // таким образом телеграмбот сможет передать входящее сообщение в контроллер, а он сможет передать ответы обратно в телеграмбот
    private UpdateController updateController;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        log.debug(originalMessage.getText());

        var response = new SendMessage();                               //View object
        response.setChatId(originalMessage.getChatId().toString());
        response.setText("Hello from bot backend");

        sendAnswerMessage(response);                                    //View controller

    }

    public void sendAnswerMessage(SendMessage message) {                //SendMessage => telegram's controller - View(from MVC pattern)
        if (message != null) {
            try {
                execute(message);                                        //Telegram's interactions code
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
}
