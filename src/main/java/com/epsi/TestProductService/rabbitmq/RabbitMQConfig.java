package com.epsi.TestProductService.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    public static final String QUEUE = "rpc_product";
    public static final String EXCHANGE = "rpc_exchange";
    public static final String ROUTING_KEY = "test.dim";

    @Bean
    public Queue queue() {
        logger.info("Queue déclarée : {}", QUEUE);
        return new Queue(QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        logger.info("Exchange déclaré : {}", EXCHANGE);
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        logger.info("Binding queue '{}' → exchange '{}' via routing key '{}'", QUEUE, EXCHANGE, ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

//    public static final String COMMANDE_PRODUIT_QUEUE = "CommandeProduit";
//
//    @Bean
//    public Queue commandeProduitQueue() {
//        return new Queue(COMMANDE_PRODUIT_QUEUE, false);
//    }

//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}


