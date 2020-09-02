package com.test.springcamelapp.model.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

@Component
public interface AbstractStrategy {
    String getCompleteURL();

    String getName();

    void setCoordinate(Coordinate coordinate);

    MessageB getMessageB(ProducerTemplate template, CamelContext camel, MessageA messageA) throws JsonProcessingException;
}

