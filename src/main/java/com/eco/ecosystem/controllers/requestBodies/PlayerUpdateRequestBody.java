package com.eco.ecosystem.controllers.requestBodies;

import com.eco.ecosystem.entities.PlayerCard;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PlayerUpdateRequestBody {
    @JsonProperty("cardsInHand")
    private List<PlayerCard> cardsInHand;
    @JsonProperty("board")
    private List<List<PlayerCard>> board;
    @JsonProperty("pointCount")
    private int pointCount;
}
