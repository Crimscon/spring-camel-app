package com.test.springcamelapp.service;

import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.Weather;
import com.test.springcamelapp.model.strategy.AbstractStrategy;
import com.test.springcamelapp.model.strategy.OpenWeatherStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
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

        camel.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:httpRoute")
                        .log("Http Route started")
                        .setHeader(Exchange.HTTP_METHOD).constant(HttpMethod.GET)
                        .to("http://" + strategy.getCompleteURL())
                        .log("Response : ${body}");
            }
        });

        camel.start();
        camel.stop();
    }

}
