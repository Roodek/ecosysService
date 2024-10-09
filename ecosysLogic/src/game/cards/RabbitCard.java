package game.cards;

public class RabbitCard extends BasicCard implements Card  {
    public RabbitCard() {
        super(CardType.RABBIT);
    }
    public static final Integer POINTS_PER_VALID=1;
    @Override
    public Integer count() {
        return POINTS_PER_VALID;
    }
}
