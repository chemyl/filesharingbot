package org.amaltsev.controller;

import lombok.extern.log4j.Log4j;
import org.amaltsev.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    public void registerBot(TelegramBot telegramBot) {      //connect updatecontroller and telegrambot
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {              // валидация типов входящих сообщений (из приватных чатов, редактированные
        // сообщения, из каналов и групп...)
        if (update == null) {
            log.error("Received updated is null");
            return;
        }
        if (update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessageByType(
            Update update) {           //распределить входящие сообщения для брокера в зависимости от типа входящих данных
        var message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getDocument() != null) {
            processDocumentMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedContentMessage(update);
        }
        //чтобы вернуть ответ, его надо сгенерировать=>  надо снова сделать объект класса SendMessage, засетить необзодимые данные и
        // передать в телеграмбот => MessageUtils
    }

    private void setUnsupportedContentMessage(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Unsupported context");
        setView(sendMessage);

    }

    private void setView(SendMessage sendMessage) {             //Проксирующий метод для проброски сообщений. Тк в последующем мы не
        // сможем вызывать ветоды телеги из других сервисов
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
    }

    private void processDocumentMessage(Update update) {
    }

    private void processTextMessage(Update update) {
    }


}
