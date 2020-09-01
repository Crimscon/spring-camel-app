package com.test.springcamelapp.model.strategy;

import com.test.springcamelapp.model.MessageA;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.springframework.stereotype.Component;

@Component
public interface AbstractStrategy {
    String getCompleteURL();

    String getName();

    void setCoordinate(Coordinate coordinate);

    RoutesBuilder getRoute(CamelContext camel, MessageA messageA);
}

