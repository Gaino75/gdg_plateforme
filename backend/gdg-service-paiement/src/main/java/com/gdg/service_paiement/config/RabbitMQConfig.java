package com.gdg.service_paiement.config;

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
    public static final String KEY_PAIEMENT_CONFIRME = "paiement.confirme";
    public static final String KEY_PAIEMENT_ECHOUE = "paiement.echoue";

        // ============================================================
    // QUEUES - AJOUTER
    // ============================================================
    public static final String QUEUE_PAIEMENT_CONFIRME = "queue.paiement.confirme";
    public static final String QUEUE_PAIEMENT_ECHOUE = "queue.paiement.echoue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    
    @Bean
    public Queue queuePaiementConfirme() {
        return new Queue(QUEUE_PAIEMENT_CONFIRME, true);
    }

    @Bean
    public Queue queuePaiementEchoue() {
        return new Queue(QUEUE_PAIEMENT_ECHOUE, true);
    }

    @Bean
    public Binding bindingPaiementConfirme() {
        return BindingBuilder
            .bind(queuePaiementConfirme())
            .to(exchange())
            .with(KEY_PAIEMENT_CONFIRME);
    }

    @Bean
    public Binding bindingPaiementEchoue() {
        return BindingBuilder
            .bind(queuePaiementEchoue())
            .to(exchange())
            .with(KEY_PAIEMENT_ECHOUE);
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
