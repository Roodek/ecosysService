package game.cards;

public class RiverCard extends FieldCard implements Card  {

    public static int RIVER_1ST_PLACE_POINTS = 8;
    public static int RIVER_2ND_PLACE_POINTS = 5;
    public RiverCard() {
        super(CardType.RIVER);
    }

    @Override
    public Integer count() {
        return super.count();
    }

}
