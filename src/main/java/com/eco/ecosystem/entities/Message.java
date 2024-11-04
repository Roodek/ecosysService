package com.eco.ecosystem.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Message {
    private String from;
    private String content;

}
