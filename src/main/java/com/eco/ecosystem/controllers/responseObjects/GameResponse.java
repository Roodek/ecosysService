package com.eco.ecosystem.controllers.responseObjects;

import com.eco.ecosystem.entities.Player;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class GameResponse {
    @Id
    UUID id;
    @JsonProperty("players")
    List<Player> players;
    @JsonProperty("turn")
    int turn;
}
