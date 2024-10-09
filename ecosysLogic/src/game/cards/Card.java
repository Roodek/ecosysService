package game.cards;

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
}
