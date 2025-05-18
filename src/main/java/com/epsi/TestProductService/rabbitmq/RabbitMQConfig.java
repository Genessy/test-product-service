package com.epsi.TestProductService.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String COMMANDE_PRODUIT_QUEUE = "CommandeProduit";

    @Bean
    public Queue commandeProduitQueue() {
        return new Queue(COMMANDE_PRODUIT_QUEUE, false);
    }

//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}


