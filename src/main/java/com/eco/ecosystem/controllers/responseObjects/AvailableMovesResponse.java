package com.eco.ecosystem.controllers.responseObjects;

import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.board.Slot;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Setter
@Getter
public class AvailableMovesResponse {
    @JsonProperty("cardsInHand")
    private List<PlayerCard> cardsInHand;
    @JsonProperty("availableSlots")
    private List<Slot> availableSlots;

}
