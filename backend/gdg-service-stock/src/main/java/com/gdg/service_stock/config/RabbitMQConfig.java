package com.gdg.service_stock.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "gdg.exchange";
    public static final String KEY_STOCK_CRITIQUE = "stock.critique";
    public static final String KEY_STOCK_DISPONIBLE = "stock.disponible";
    
    // ============================================================
    // QUEUES - AJOUTER
    // ============================================================
    public static final String QUEUE_STOCK_CRITIQUE = "queue.stock.critique";
    public static final String QUEUE_STOCK_DISPONIBLE = "queue.stock.disponible";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

        // ============================================================
    // QUEUES - AJOUTER
    // ============================================================
    @Bean
    public Queue queueStockCritique() {
        return new Queue(QUEUE_STOCK_CRITIQUE, true);
    }

    @Bean
    public Queue queueStockDisponible() {
        return new Queue(QUEUE_STOCK_DISPONIBLE, true);
    }

    // ============================================================
    // BINDINGS - AJOUTER
    // ============================================================
    @Bean
    public Binding bindingStockCritique() {
        return BindingBuilder
            .bind(queueStockCritique())
            .to(exchange())
            .with(KEY_STOCK_CRITIQUE);
    }

    @Bean
    public Binding bindingStockDisponible() {
        return BindingBuilder
            .bind(queueStockDisponible())
            .to(exchange())
            .with(KEY_STOCK_DISPONIBLE);
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
