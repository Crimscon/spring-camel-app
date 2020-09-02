package com.test.springcamelapp.model;

import com.test.springcamelapp.model.strategy.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageA {
    private String message;
    private Lang lang;
    private Coordinate coordinate;
}
