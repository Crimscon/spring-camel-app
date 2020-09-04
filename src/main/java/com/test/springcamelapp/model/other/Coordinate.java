package com.test.springcamelapp.model.other;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Coordinate {
    @EqualsAndHashCode.Include
    private String lat = "";

    @EqualsAndHashCode.Include
    private String lon = "";

    public Coordinate(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }
}