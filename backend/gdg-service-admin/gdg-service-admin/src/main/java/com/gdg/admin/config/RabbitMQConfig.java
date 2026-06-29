package com.gdg.admin.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "gdg.exchange";

    public static final String QUEUE_USER_SUSPENDED = "queue.user.suspended";
    public static final String QUEUE_AGENCE_VALIDEE = "queue.agence.validee";

    public static final String KEY_USER_SUSPENDED = "user.suspended";
    public static final String KEY_AGENCE_VALIDEE = "agence.validee";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queueUserSuspended() {
        return new Queue(QUEUE_USER_SUSPENDED, true);
    }

    @Bean
    public Queue queueAgenceValidee() {
        return new Queue(QUEUE_AGENCE_VALIDEE, true);
    }

    @Bean
    public Binding bindingUserSuspended() {
        return BindingBuilder
            .bind(queueUserSuspended())
            .to(exchange())
            .with(KEY_USER_SUSPENDED);
    }

    @Bean
    public Binding bindingAgenceValidee() {
        return BindingBuilder
            .bind(queueAgenceValidee())
            .to(exchange())
            .with(KEY_AGENCE_VALIDEE);
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