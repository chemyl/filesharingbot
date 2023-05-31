package org.amaltsev.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter() {    //возвращает jsonSonverter который преобразует update в Json и передавать их в
        // rabbitMQ. При получении апдейтов обратно вприложение, оно будет преобразовывать в Java объекты.
        return new Jackson2JsonMessageConverter();
    }

    //    Настройка очередей, при первичном подключении к брокеру, эти очереди создадутся.
    //    При последующих подклчюениях к брокеру, сообщения будут писаться в уже заготовленные и полные очрееди в конец списка.
}
