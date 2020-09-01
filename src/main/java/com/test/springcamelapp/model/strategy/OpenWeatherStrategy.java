package com.test.springcamelapp.model.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OpenWeatherStrategy implements AbstractStrategy {

    private final String name = "OpenWeather";

    @Value("${openWeather.token}")
    private String token;

    @Value("${openWeather.format}")
    private String format;

    private Coordinate coordinate;

    public OpenWeatherStrategy(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String getCompleteURL() {
        String formatUrl = getFormat();
        return formatUrl.replace("{lat}", getCoordinate().getLat())
                .replace("{lon}", getCoordinate().getLon())
                .replace("{token}", getToken());
    }

    @Override
    public RoutesBuilder getRoute(CamelContext camel, MessageA messageA) {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(getCompleteURL())
                        .routeId(getName())
                        .process(exchange -> {
                            Message in = exchange.getIn();
                            Object body = in.getBody();

                            DefaultMessage message = new DefaultMessage(exchange);
                            message.setBody(createMessageB(body, messageA));
                        })
                        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                        .setBody(body())
                        .to("http://localhost:8081/messageB");
            }
        };
    }

    private Object createMessageB(Object body, MessageA messageA) throws JsonProcessingException {
        MessageB messageB = new MessageB();
        messageB.setMessage(messageA.getMessage());
        messageB.setDateTime(LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(body.toString());

        String stringTemperature = actualObj.get("main").get("temp").toString();
        Double temperature = 273.15 - Double.parseDouble(stringTemperature);
        messageB.setTemperature((int) Math.floor(temperature));

        return messageB;
    }
}

