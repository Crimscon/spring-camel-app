package com.test.springcamelapp.model.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.springcamelapp.model.MessageA;
import com.test.springcamelapp.model.MessageB;
import com.test.springcamelapp.model.other.Coordinate;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

@Component
public interface AbstractService {
    String getCompleteURL(Coordinate coordinate);

    String getName();

    MessageB getMessageB(ProducerTemplate template, CamelContext camel, MessageA messageA) throws JsonProcessingException;

    default Message getMessage(ProducerTemplate template, CamelContext camel, Coordinate coordinate) {
        return template.request(getCompleteURL(coordinate), camel.getProcessor(getName())).getMessage();
    }

}