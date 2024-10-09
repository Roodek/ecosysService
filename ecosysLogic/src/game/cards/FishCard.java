package game.cards;

public class FishCard extends BasicCard implements Card  {
    public FishCard() {
        super(CardType.FISH);
    }

    public static final Integer POINTS_PER_VALID=2;
    @Override
    public Integer count() {
        return (int)(getNeighbours().stream()
                .filter(neighbour->neighbour.getType()==CardType.DRAGONFLY || neighbour.getType()==CardType.RIVER)
                .count() * POINTS_PER_VALID);
    }
}
