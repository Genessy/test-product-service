package com.epsi.msproduct.rabbitmq;

import com.epsi.msproduct.dto.order.incoming.OrderMessageDto;
import com.epsi.msproduct.dto.order.outgoing.ProductOrderResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.epsi.msproduct.service.ProductService;

@Component
public class ProductRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(ProductRpcServer.class);
    private ProductService productService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public String handleStockCheck(String messageJson) {
        logger.info("Message du service Commande reçu : {}", messageJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            OrderMessageDto orderMessage = mapper.readValue(messageJson, OrderMessageDto.class);

            ProductOrderResponseDto responseDto = productService.handleOrderCheck(orderMessage);

            String responseJson = mapper.writeValueAsString(responseDto);
            logger.info("Réponse envoyée : {}", responseJson);
            return responseJson;

        } catch (Exception e) {
            logger.error("Erreur pendant le traitement RPC", e);
            return "{\"status\":\"error\"}";
        }
    }
}
