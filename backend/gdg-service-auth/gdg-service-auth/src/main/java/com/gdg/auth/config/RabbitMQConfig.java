package com.gdg.auth.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    // Exchange principal
    public static final String EXCHANGE = "gdg.exchange";

    // Queues
    public static final String QUEUE_USER_REGISTERED = "queue.user.registered";
    public static final String QUEUE_PASSWORD_RESET = "queue.password.reset";

    // Routing keys
    public static final String KEY_USER_REGISTERED = "user.registered";
    public static final String KEY_PASSWORD_RESET = "password.reset";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queueUserRegistered() {
        return new Queue(QUEUE_USER_REGISTERED, true);
    }

    @Bean
    public Queue queuePasswordReset() {
        return new Queue(QUEUE_PASSWORD_RESET, true);
    }

    @Bean
    public Binding bindingUserRegistered() {
        return BindingBuilder
            .bind(queueUserRegistered())
            .to(exchange())
            .with(KEY_USER_REGISTERED);
    }

    @Bean
    public Binding bindingPasswordReset() {
        return BindingBuilder
            .bind(queuePasswordReset())
            .to(exchange())
            .with(KEY_PASSWORD_RESET);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}