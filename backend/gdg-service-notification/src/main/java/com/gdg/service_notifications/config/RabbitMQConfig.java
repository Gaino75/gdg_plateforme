package com.gdg.service_notifications.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "gdg.exchange";

    public static final String QUEUE_USER_REGISTERED = "queue.user.registered";
    public static final String QUEUE_PASSWORD_RESET = "queue.password.reset";
    public static final String QUEUE_USER_SUSPENDED = "queue.user.suspended";
    public static final String QUEUE_AGENCE_VALIDEE = "queue.agence.validee";

    public static final String KEY_USER_REGISTERED = "user.registered";
    public static final String KEY_PASSWORD_RESET = "password.reset";
    public static final String KEY_USER_SUSPENDED = "user.suspended";
    public static final String KEY_AGENCE_VALIDEE = "agence.validee";

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
    public Queue queueUserSuspended() {
        return new Queue(QUEUE_USER_SUSPENDED, true);
    }

    @Bean
    public Queue queueAgenceValidee() {
        return new Queue(QUEUE_AGENCE_VALIDEE, true);
    }

    @Bean
    public Binding bindingUserRegistered() {
        return BindingBuilder.bind(queueUserRegistered())
            .to(exchange()).with(KEY_USER_REGISTERED);
    }

    @Bean
    public Binding bindingPasswordReset() {
        return BindingBuilder.bind(queuePasswordReset())
            .to(exchange()).with(KEY_PASSWORD_RESET);
    }

    @Bean
    public Binding bindingUserSuspended() {
        return BindingBuilder.bind(queueUserSuspended())
            .to(exchange()).with(KEY_USER_SUSPENDED);
    }

    @Bean
    public Binding bindingAgenceValidee() {
        return BindingBuilder.bind(queueAgenceValidee())
            .to(exchange()).with(KEY_AGENCE_VALIDEE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
