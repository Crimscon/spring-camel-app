package com.test.springcamelapp.model.strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
}

