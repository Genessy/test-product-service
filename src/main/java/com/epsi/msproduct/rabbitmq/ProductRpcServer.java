package com.epsi.msproduct.rabbitmq;

import com.epsi.msproduct.dto.order.incoming.OrderMessageDto;
import com.epsi.msproduct.dto.order.outgoing.ProductOrderResponseDto;
import com.epsi.msproduct.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(ProductRpcServer.class);
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;
    private final OpenTelemetry openTelemetry;

    public ProductRpcServer(ProductService productService, RabbitTemplate rabbitTemplate, Tracer tracer, OpenTelemetry openTelemetry) {
        this.productService = productService;
        this.rabbitTemplate = rabbitTemplate;
        this.tracer = tracer;
        this.openTelemetry = openTelemetry;
    }

    private final TextMapGetter<MessageProperties> messagePropertiesGetter = new TextMapGetter<MessageProperties>() {
        @Override
        public Iterable<String> keys(MessageProperties carrier) {
            return carrier.getHeaders().keySet();
        }

        @Override
        public String get(MessageProperties carrier, String key) {
            Object headerValue = carrier.getHeaders().get(key);
            return headerValue == null ? null : headerValue.toString();
        }
    };

    private final TextMapSetter<MessageProperties> messagePropertiesSetter = (carrier, key, value) -> {
        if (carrier != null) {
            carrier.setHeader(key, value);
        }
    };

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleStockCheck(Message message) {
        String payload = new String(message.getBody());
        logger.info("Message du service Commande reçu : {}", payload);

        Context extractedContext = openTelemetry.getPropagators().getTextMapPropagator()
                .extract(Context.current(), message.getMessageProperties(), messagePropertiesGetter);

        Span span = null;
        try (Scope scope = extractedContext.makeCurrent()) {
            span = tracer.spanBuilder("rpc-product-server-process")
                    .setParent(extractedContext)
                    .setSpanKind(SpanKind.SERVER)
                    .startSpan();

            try (Scope spanScope = span.makeCurrent()) {
                ObjectMapper mapper = new ObjectMapper();
                OrderMessageDto orderMessage = mapper.readValue(payload, OrderMessageDto.class);

                ProductOrderResponseDto responseDto = productService.handleOrderCheck(orderMessage);
                String responseJson = mapper.writeValueAsString(responseDto);

                String replyTo = message.getMessageProperties().getReplyTo();
                String correlationId = message.getMessageProperties().getCorrelationId();

                MessageProperties props = new MessageProperties();
                props.setCorrelationId(correlationId);

                openTelemetry.getPropagators().getTextMapPropagator()
                        .inject(Context.current(), props, messagePropertiesSetter);

                Message responseMessage = new Message(responseJson.getBytes(), props);

                rabbitTemplate.send("", replyTo, responseMessage);

                logger.info("Réponse envoyée manuellement à [{}] : {}", replyTo, responseJson);
            }
        } catch (Exception e) {
            if (span != null) {
                span.recordException(e);
            }
            logger.error("Erreur pendant le traitement RPC", e);
        } finally {
            if (span != null) {
                span.end();
            }
        }
    }
}