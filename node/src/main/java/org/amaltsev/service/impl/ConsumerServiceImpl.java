package org.amaltsev.service.impl;

import lombok.extern.log4j.Log4j;
import org.amaltsev.service.ConsumerService;
import org.amaltsev.service.ProducerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.amaltsev.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final ProducerService producerService;

    public ConsumerServiceImpl(ProducerService producerService) {
        this.producerService = producerService;
    }


    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
         var message = update.getMessage();
         var sendMessage = new SendMessage();
         sendMessage.setChatId(message.getChatId().toString());
         sendMessage.setText("HELLo from NODE");
         producerService.producerAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: doc message is received");
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: photo message is received");
    }
}
