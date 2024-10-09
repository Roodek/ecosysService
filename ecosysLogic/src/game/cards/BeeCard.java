package game.cards;

public class BeeCard extends BasicCard implements Card {
    public BeeCard() {
        super(CardType.BEE);
    }
    public static final Integer POINTS_PER_VALID=3;
    @Override
    public Integer count() {
        return (int)(getNeighbours().stream()
                .filter(neighbour->neighbour.getType()==CardType.MEADOW)
                .count() * POINTS_PER_VALID);
    }
}
