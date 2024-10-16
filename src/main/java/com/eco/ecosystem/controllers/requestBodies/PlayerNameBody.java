package com.eco.ecosystem.controllers.requestBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PlayerNameBody {
    @JsonProperty("playerName")
    String playerName;
}
