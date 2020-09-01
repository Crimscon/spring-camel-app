package com.test.springcamelapp.service;

import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.strategy.AbstractStrategy;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final AbstractStrategy strategy;

    public WeatherService(@Qualifier("openWeatherStrategy") AbstractStrategy strategy) {
        this.strategy = strategy;
    }

    public void getWeather(MessageA messageA) throws Exception {
        strategy.setCoordinate(messageA.getCoordinate());

        CamelContext camel = new DefaultCamelContext();
        ProducerTemplate template = camel.createProducerTemplate();

        camel.addRoutes(strategy.getRoute(camel, messageA));

        camel.start();

        Thread.sleep(5000);

        camel.stop();

    }

}
