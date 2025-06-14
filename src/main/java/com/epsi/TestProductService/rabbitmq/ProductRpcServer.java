package com.epsi.TestProductService.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(ProductRpcServer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public String test(String message){
        logger.info("Message du service Commande reçu : {}", message);

        try {
            String reponse = "ok";
            logger.info("Traitement terminé avec succès");
            logger.info("Envoi de la réponse : {}", reponse);
            return reponse;
        } catch (Exception e) {
            logger.info("Erreur pendant le traitement RPC", e);
            return "error";
        }
    }
}
