package com.eco.ecosystem.entities;

import com.eco.ecosystem.game.cards.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FinalGeneralCount {
    private Map<Card.CardType, Integer> cardCount;
    private int ecosystemGaps;
    private int gapPoints;
}
