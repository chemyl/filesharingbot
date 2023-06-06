package org.amaltsev.controller;

import lombok.extern.log4j.Log4j;
import org.amaltsev.service.UpdateProducer;
import org.amaltsev.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.amaltsev.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
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
        if (update.hasMessage()) {
            distributeMessageByType(update);
        } else {
            log.error("Usupported type is reseived: " + update);
        }
    }

    private void distributeMessageByType(
            Update update) {           //распределить входящие сообщения для брокера в зависимости от типа входящих данных
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocumentMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedContentMessage(update);
        }
        //чтобы вернуть ответ, его надо сгенерировать=>  надо снова сделать объект класса SendMessage, засетить необзодимые данные и
        // передать в телеграмбот => MessageUtils
    }

    public void setView(SendMessage sendMessage) {             //Проксирующий метод для проброски сообщений. Тк в последующем мы не
        // сможем вызывать ветоды телеги из других сервисов и/или захотим добавить централизованную промежуточную логику в этот метод для
        // всех вызовов
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {         //овтет для пользователя, что его отправленный контент обрабатывается
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Файл получен, идет обработка...");
        setView(sendMessage);
    }

    private void setUnsupportedContentMessage(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Unsupported context");
        setView(sendMessage);
    }

    private void processPhotoMessage(Update update) {       // передача каждого типа данных в нужную очередь
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocumentMessage(Update update) {        // передача каждого типа данных в нужную очередь
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void  processTextMessage(Update update) {        // передача каждого типа данных в нужную очередь
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }
}
