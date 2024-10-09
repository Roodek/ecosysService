package game.cards;

public class ElkCard extends BasicCard implements Card  {

    public static final Integer POINTS_PER_VALID=2;
    public ElkCard() {
        super(CardType.ELK);
    }
}
