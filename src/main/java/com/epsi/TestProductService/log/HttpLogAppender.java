package com.epsi.TestProductService.log;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class HttpLogAppender extends AppenderBase<ILoggingEvent> {

    private final RestTemplate restTemplate = new RestTemplate();
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String payload = String.format("{\"timestamp\":\"%s\",\"level\":\"%s\",\"logger\":\"%s\",\"thread\":\"%s\",\"message\":\"%s\"}",
                    new java.util.Date(eventObject.getTimeStamp()),
                    eventObject.getLevel(),
                    eventObject.getLoggerName(),
                    eventObject.getThreadName(),
                    eventObject.getFormattedMessage().replace("\"", "\\\""));

            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Erreur d'envoi du log vers Logstash : " + e.getMessage());
        }
    }
}
