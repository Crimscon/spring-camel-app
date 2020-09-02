package com.test.springcamelapp.controller;

import com.test.springcamelapp.model.Lang;
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
    public ResponseEntity<MessageB> weather(@RequestBody MessageA message) throws Exception {
        if (message.getLang() == Lang.RU && !message.getMessage().isEmpty())
            return new ResponseEntity<>(weatherService.getWeather(message), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
