package com.eco.ecosystem.entities;

import com.eco.ecosystem.game.cards.Card;
import com.eco.ecosystem.game.exceptions.InvalidCardTypeException;
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

    @Override
    public boolean equals(Object obj) {
        return obj.getClass()==this.getClass() && ((PlayerCard) obj).getCardType().equals(cardType);
    }

    public Card toCard() throws InvalidCardTypeException {
        return Card.fromString(cardType);
    }
}
