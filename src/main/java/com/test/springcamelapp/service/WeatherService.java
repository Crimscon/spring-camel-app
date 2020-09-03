package com.test.springcamelapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.strategy.AbstractStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final AbstractStrategy strategy;

    @Value("${serviceB.url}")
    private String urlService;

    public WeatherService(@Qualifier("openWeatherStrategy") AbstractStrategy strategy) {
        this.strategy = strategy;
    }

    public MessageB getWeather(MessageA messageA) throws Exception {
        strategy.setCoordinate(messageA.getCoordinate());

        CamelContext camel = new DefaultCamelContext();
        ProducerTemplate template = camel.createProducerTemplate();

        camel.start();

        MessageB messageB = strategy.getMessageB(template, camel, messageA);
        template
                .sendBodyAndHeaders(
                        urlService + "receiveMessage",
                        new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(messageB),
                        strategy.getMessage(template, camel).getHeaders());

        camel.stop();

        return messageB;
    }

}
