package com.test.springcamelapp.model;

import com.test.springcamelapp.model.strategy.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Weather {

    private Integer temp;
    private Coordinate coord;

}
