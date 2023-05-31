package org.amaltsev.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ProducerService {

    //    отправка ответов с ноды в брокер
    void producerAnswer(SendMessage sendMessage);

}
