package com.test.springcamelapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageB {
    private String message = "";
    private LocalDateTime dateTime;
    private Integer temperature;
}
