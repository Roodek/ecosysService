package com.eco.ecosystem.controllers.requestBodies;

import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutCardRequestBody {
    Card.CardType cardType;
    Slot slot;
}
