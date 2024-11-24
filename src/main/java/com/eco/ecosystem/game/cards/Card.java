package com.eco.ecosystem.game.cards;

import com.eco.ecosystem.entities.PlayerCard;
import com.eco.ecosystem.game.exceptions.InvalidCardTypeException;

import java.util.List;

public interface Card {
    enum CardType{
        BEE,
        BEAR,
        DRAGONFLY,
        EAGLE,
        ELK,
        FISH,
        FOX,
        MEADOW,
        RABBIT,
        RIVER,
        WOLF
    }

    Card getUpperNeighbour();
    Card getBottomNeighbour();
    Card getLeftNeighbour();
    Card getRightNeighbour();
    void setRightNeighbour(Card card);
    void setLeftNeighbour(Card card);
    void setUpperNeighbour(Card card);
    void setBottomNeighbour(Card card);

    List<Card> getNeighbours();


    Integer count();
    CardType getType();
    static Card fromString(String cardType) throws InvalidCardTypeException {
        try {
            return cardType==null?null: switch (CardType.valueOf(cardType.toUpperCase().trim())) {
                case BEE -> new BeeCard();
                case BEAR -> new BearCard();
                case DRAGONFLY -> new DragonflyCard();
                case EAGLE -> new EagleCard();
                case ELK -> new ElkCard();
                case FISH -> new FishCard();
                case FOX -> new FoxCard();
                case MEADOW -> new MeadowCard();
                case RABBIT -> new RabbitCard();
                case RIVER -> new RiverCard();
                case WOLF -> new WolfCard();
            };
        }catch (IllegalArgumentException e){
            throw new InvalidCardTypeException();
        }
    }
    static Card from(CardType cardType) {
        return cardType==null?null: switch (cardType) {
            case BEE -> new BeeCard();
            case BEAR -> new BearCard();
            case DRAGONFLY -> new DragonflyCard();
            case EAGLE -> new EagleCard();
            case ELK -> new ElkCard();
            case FISH -> new FishCard();
            case FOX -> new FoxCard();
            case MEADOW -> new MeadowCard();
            case RABBIT -> new RabbitCard();
            case RIVER -> new RiverCard();
            case WOLF -> new WolfCard();
        };
    }

    static PlayerCard toPlayerCard(CardType cardType){
        return new PlayerCard(cardType.toString());
    }


}
