package game.cards;

public class WolfCard extends BasicCard implements Card  {

    public static int WOLF_1ST_PLACE_POINTS = 12;
    public static int WOLF_2ND_PLACE_POINTS = 8;
    public static int WOLF_3RD_PLACE_POINTS = 4;
    public WolfCard() {
        super(CardType.WOLF);
    }
}
