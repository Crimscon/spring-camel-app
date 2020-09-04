package com.test.springcamelapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.strategy.AbstractService;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final AbstractService strategy;

    @Value("${serviceB.url}")
    private String urlService;

    public WeatherService(@Qualifier("openWeatherService") AbstractService strategy) {
        this.strategy = strategy;
    }

    public ResponseEntity<MessageB> getWeather(MessageA messageA) throws JsonProcessingException {
        strategy.setCoordinate(messageA.getCoordinate());

        CamelContext camel = new DefaultCamelContext();
        ProducerTemplate template = camel.createProducerTemplate();

        camel.start();
        MessageB messageB;

        try {
            messageB = strategy.getMessageB(template, camel, messageA);
        } catch (JsonProcessingException e) {
            camel.stop();

            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        template
                .sendBodyAndHeaders(
                        urlService + "receiveMessage",
                        new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(messageB),
                        strategy.getMessage(template, camel).getHeaders());


        camel.stop();

        return new ResponseEntity<>(messageB, HttpStatus.OK);
    }

}
