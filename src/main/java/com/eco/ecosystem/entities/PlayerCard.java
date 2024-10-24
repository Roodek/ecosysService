package com.eco.ecosystem.entities;

import com.eco.ecosystem.game.cards.Card;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCard {
    @JsonProperty("cardType")
    String cardType;

    public PlayerCard(Card card){
        this.cardType = card.toString();
    }
}
