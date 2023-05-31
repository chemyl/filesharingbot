package org.amaltsev.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.amaltsev.model.RabbitQueue.ANSWER_MESSAGE;
import static org.amaltsev.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static org.amaltsev.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {    //возвращает jsonSonverter который преобразует update в Json и передавать их в
        // rabbitMQ. При получении апдейтов обратно вприложение, оно будет преобразовывать в Java объекты.
        return new Jackson2JsonMessageConverter();
    }

    //    Настройка очередей, при первичном подключении к брокеру, эти очереди создадутся.
    //    При последующих подклчюениях к брокеру, сообщения будут писаться в уже заготовленные и полные очрееди в конец списка.
    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue docMessageQueue() {
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    @Bean
    public Queue photoMessageQueue() {
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE);
    }
}
