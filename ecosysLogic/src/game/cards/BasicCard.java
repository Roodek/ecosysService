package game.cards;


import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class BasicCard implements Card{
    CardType type;
    Card upperNeighbour = null;
    Card bottomNeighbour = null;
    Card leftNeighbour = null;
    Card rightNeighbour = null;
    BasicCard(CardType type){
        this.type = type;
    }

    @Override
    public List<Card> getNeighbours(){
        return Stream.of(upperNeighbour,bottomNeighbour,leftNeighbour,rightNeighbour).filter(Objects::nonNull).toList();
    }
    @Override
    public Card getUpperNeighbour() {
        return this.upperNeighbour;
    }

    @Override
    public Card getBottomNeighbour() {
        return this.bottomNeighbour;
    }

    @Override
    public Card getLeftNeighbour() {
        return this.leftNeighbour;
    }

    @Override
    public Card getRightNeighbour() {
        return this.rightNeighbour;
    }

    @Override
    public void setUpperNeighbour(Card upperNeighbour) {
        this.upperNeighbour = upperNeighbour;
    }

    @Override
    public void setBottomNeighbour(Card bottomNeighbour) {
        this.bottomNeighbour = bottomNeighbour;
    }

    @Override
    public void setLeftNeighbour(Card leftNeighbour) {
        this.leftNeighbour = leftNeighbour;
    }

    @Override
    public void setRightNeighbour(Card rightNeighbour) {
        this.rightNeighbour = rightNeighbour;
    }

    @Override
    public CardType getType() {
        return this.type;
    }

    @Override
    public Integer count() {
        return 0;
    }
}
