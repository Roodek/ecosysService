package game.cards;

public class MeadowCard extends FieldCard implements Card {
    public MeadowCard() {
        super(CardType.MEADOW);
    }

    @Override
    public Integer count() {
        if (this.isCalculated()) {
            return 0;
        } else {
            propagateCalculated();
            return switch (this.getFieldSize()) {
                case 0:
                case 1:
                    yield 0;
                case 2:
                    yield 3;
                case 3:
                    yield 6;
                case 4:
                    yield 10;
                default:
                    yield 15;
            };
        }
    }
}
