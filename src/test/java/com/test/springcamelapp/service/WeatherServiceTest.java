package com.test.springcamelapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.springcamelapp.model.Lang;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.strategy.AbstractStrategy;
import com.test.springcamelapp.model.strategy.Coordinate;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Component
public class WeatherServiceTest {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @MockBean
    private AbstractStrategy strategy;
    @MockBean
    private CamelContext camel;
    @MockBean
    private ProducerTemplate template;
    @Value("${serviceB.url}")
    private String urlService;
    private MessageA messageA;
    private MessageB messageB = new MessageB();

    @Test
    public void createMessageATest() {
        messageA = createMessageAForTest("Hi!", Lang.RU, createTestCoordinate("55.21", "67.01"));

        Assert.assertNotNull(messageA);
    }

    @Test
    public void getMessageB() throws JsonProcessingException {
        messageA = createMessageAForTest("Hi!", Lang.RU, createTestCoordinate("55.21", "67.01"));

        camel.start();

        messageB = strategy.getMessageB(template, camel, messageA);

        Mockito.verify(strategy, Mockito.times(1))
                .getMessageB(
                        Mockito.any(ProducerTemplate.class),
                        Mockito.any(CamelContext.class),
                        Mockito.any(MessageA.class));

        camel.stop();
    }

    @Test
    public void createRequest() throws JsonProcessingException {
        camel.start();

        messageB.setMessage("SomeText");
        messageB.setDateTime(LocalDateTime.now());
        messageB.setTemperature(10);

        template.sendBodyAndHeaders(urlService + "receiveMessage",
                mapper.writeValueAsString(messageB), new HashMap<>());

        Mockito.verify(template)
                .sendBodyAndHeaders(Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyMap());

        camel.stop();
    }

    public static MessageA createMessageAForTest(String message, Lang lang, Coordinate coordinate) {
        MessageA messageA = new MessageA();
        messageA.setMessage(message);
        messageA.setLang(lang);
        messageA.setCoordinate(coordinate);

        return messageA;
    }

    public static Coordinate createTestCoordinate(String lat, String lon) {
        return new Coordinate(lat, lon);
    }
}