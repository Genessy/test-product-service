package com.epsi.TestProductService.rabbitmq;

import com.epsi.TestProductService.dto.ProductOrderDto;
import com.epsi.TestProductService.service.ProductService;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProductConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductConsumer.class);

//    @RabbitListener(queues = RabbitMQConfig.COMMANDE_PRODUIT_QUEUE)
//    public void consummerOrderProduct(ProductOrderDto message) throws ExecutionException, InterruptedException {
//        System.out.println("Message reçu : Produit ID = " + message.getProductId() + ", Quantité = " + message.getStock());
//
//        boolean commandeIsOk = ProductService.manageProductOrders(message.getProductId(), message.getStock());
//
//        if (commandeIsOk) {
//            System.out.println("Commande acceptée : stock mis à jour");
//        } else {
//            System.out.println("Commande refusée : stock insuffisant");
//        }
//    }
//}

//    @RabbitListener(queues = RabbitMQConfig.COMMANDE_PRODUIT_QUEUE)
//    public void consummerOrderProduct(String message) {
//        System.out.println("Message texte brut reçu : " + message);
//        logger.info("Received raw order message: {}", message);
//    }
}