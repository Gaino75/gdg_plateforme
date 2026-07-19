package com.gdg.service_reservations.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "gdg.exchange";
    public static final String KEY_RESERVATION_CONFIRMEE = "reservation.confirmee";

    // ============================================================
    // QUEUES - AJOUTER
    // ============================================================
    public static final String QUEUE_RESERVATION_CONFIRMEE = "queue.reservation.confirmee";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }
    
    // ============================================================
    // QUEUE - AJOUTER
    // ============================================================
    @Bean
    public Queue queueReservationConfirmee() {
        return new Queue(QUEUE_RESERVATION_CONFIRMEE, true);
    }

    // ============================================================
    // BINDING - AJOUTER
    // ============================================================
    @Bean
    public Binding bindingReservationConfirmee() {
        return BindingBuilder
            .bind(queueReservationConfirmee())
            .to(exchange())
            .with(KEY_RESERVATION_CONFIRMEE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
