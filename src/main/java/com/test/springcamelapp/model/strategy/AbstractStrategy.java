package com.test.springcamelapp.model.strategy;

import org.springframework.stereotype.Component;

@Component
public interface AbstractStrategy {
    String getCompleteURL();
    String getName();
    void setCoordinate(Coordinate coordinate);
}

