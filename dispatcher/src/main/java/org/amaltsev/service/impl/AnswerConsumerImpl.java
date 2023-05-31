package org.amaltsev.service.impl;

import lombok.extern.log4j.Log4j;
import org.amaltsev.controller.UpdateController;
import org.amaltsev.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.amaltsev.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Log4j
public class AnswerConsumerImpl implements AnswerConsumer {

    // чтобы считать из брокера ответы, которые были отправлены из ноды.
// внедряем зависимость на апдейт контроллер, тк далее, входящие ответы должны быть переданы в апдейтКонтроллер класс
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);

    }


}
