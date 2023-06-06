package org.amaltsev.service.impl;

import lombok.extern.log4j.Log4j;
import org.amaltsev.service.UpdateProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        rabbitTemplate.convertAndSend(rabbitQueue, update);
        //бин подтягивается из ampq зависимости. После чего, спринг в процессе
        // работы его создаст и поместит в свой контекст. Вызвать на нем метод convertAndSend(имя очереди и апдейт, который преобразуется
        // в JSON и отправится в rabbitMQ )

    }
}
