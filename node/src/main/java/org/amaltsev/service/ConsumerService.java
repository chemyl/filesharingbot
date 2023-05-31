package org.amaltsev.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {

    //Считывание сообщений из брокера. под каждую очередь свой метод.
    void consumeTextMessageUpdates(Update update);

    void consumeDocMessageUpdates(Update update);

    void consumePhotoMessageUpdates(Update update);

}
