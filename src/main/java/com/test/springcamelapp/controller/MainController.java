package com.test.springcamelapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.springcamelapp.model.other.Lang;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    public final WeatherService weatherService;

    public MainController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/weather")
    public ResponseEntity<MessageB> weather(@RequestBody MessageA message) throws JsonProcessingException {
        if (message.getLang() == Lang.RU && !message.getMessage().isEmpty())
            return weatherService.getWeather(message);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
