package com.eco.ecosystem.entities;

import com.eco.ecosystem.game.board.Slot;
import com.eco.ecosystem.game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectedMove {
    Card.CardType selectedCard;
    Slot selectedSlot;
    Slot slotToSwap1;
    Slot slotToSwap2;
}
