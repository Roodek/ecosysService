package game.cards;

public class FoxCard extends BasicCard implements Card {
    public FoxCard() {
        super(CardType.FOX);
    }

    public static final Integer POINTS_PER_VALID=3;
    @Override
    public Integer count() {
        return getNeighbours().stream()
                .anyMatch(neighbour -> neighbour.getType() == CardType.WOLF || neighbour.getType() == CardType.BEAR) ?0:POINTS_PER_VALID;
    }
}
