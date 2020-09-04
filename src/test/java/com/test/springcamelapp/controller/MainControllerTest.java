package com.test.springcamelapp.controller;

import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.other.Lang;
import com.test.springcamelapp.service.WeatherService;
import com.test.springcamelapp.service.WeatherServiceTest;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WeatherService weatherService;

    // Для срабатывания теста нужен работающий сервис Б.
    @Test
    public void wrongLang() throws Exception {
        MessageA message = WeatherServiceTest.createMessageAForTest(
                "Hi",
                Lang.EN,
                WeatherServiceTest
                        .createTestCoordinate("11", "10"));

        ResponseEntity<MessageB> entity = restTemplate.postForEntity("/weather", message, MessageB.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());

        message.setLang(Lang.ES);
        entity = restTemplate.postForEntity("/weather", message, MessageB.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());

        Mockito.doReturn(new ResponseEntity<>(new MessageB(), HttpStatus.OK))
                .when(Mockito.mock(weatherService.getClass()))
                .getWeather(Mockito.any(MessageA.class));

        message.setLang(Lang.RU);
        entity = restTemplate.postForEntity("/weather", message, MessageB.class);
        Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    public void emptyMessage() {
        MessageA messageA = WeatherServiceTest.createMessageAForTest(
                "",
                Lang.RU,
                WeatherServiceTest
                        .createTestCoordinate("11", "10"));

        ResponseEntity<MessageB> entity = restTemplate.postForEntity("/weather", messageA, MessageB.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    // Для срабатывания теста нужен работающий сервис Б.
    @Test
    public void normalMessage() throws Exception {
        MessageA messageA = WeatherServiceTest.createMessageAForTest(
                "SomeMessage",
                Lang.RU,
                WeatherServiceTest
                        .createTestCoordinate("11", "10"));

        MessageB messageB = new MessageB();
        messageB.setMessage(messageA.getMessage());
        messageB.setDateTime(LocalDateTime.now());
        messageB.setTemperature(10);

        Mockito.doNothing()
                .when(Mockito.mock(ProducerTemplate.class))
                .sendBodyAndHeaders(Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyMap());

        Mockito.doReturn(new ResponseEntity<>(messageB, HttpStatus.OK))
                .when(Mockito.mock(weatherService.getClass()))
                .getWeather(messageA);


        ResponseEntity<MessageB> entity = restTemplate.postForEntity("/weather", messageA, MessageB.class);

        Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assert.assertNotNull(entity.getBody());
        Assert.assertEquals(messageA.getMessage(), entity.getBody().getMessage());
    }

}