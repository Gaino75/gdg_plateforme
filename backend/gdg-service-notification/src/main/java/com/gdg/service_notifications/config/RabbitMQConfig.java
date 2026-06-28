package com.gdg.service_notifications.config;



import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "gdg.exchange";

    public static final String QUEUE_USER_REGISTERED = "queue.user.registered";
    public static final String QUEUE_PASSWORD_RESET = "queue.password.reset";
    public static final String QUEUE_USER_SUSPENDED = "queue.user.suspended";
    public static final String QUEUE_AGENCE_VALIDEE = "queue.agence.validee";

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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}