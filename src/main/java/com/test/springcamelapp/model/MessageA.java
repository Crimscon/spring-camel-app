package com.test.springcamelapp.model;

import com.test.springcamelapp.model.other.Coordinate;
import com.test.springcamelapp.model.other.Lang;
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
