package game.cards;

public class BearCard extends BasicCard implements Card {

    private static final Integer POINTS_PER_VALID=2;
    public BearCard() {
        super(CardType.BEAR);
    }
    @Override
    public Integer count() {
        return (int)(getNeighbours().stream()
                .filter(neighbour->neighbour.getType()==CardType.FISH || neighbour.getType()==CardType.BEE)
                .count() * POINTS_PER_VALID);
    }
}
