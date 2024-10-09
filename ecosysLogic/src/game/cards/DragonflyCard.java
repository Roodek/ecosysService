package game.cards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DragonflyCard extends BasicCard implements Card {
    public DragonflyCard() {
        super(CardType.DRAGONFLY);
    }

    @Override
    public Integer count() {
        var rivers = new HashMap<String,FieldCard>();
                getNeighbours().stream()
                .filter(neighbour->neighbour.getType()==CardType.RIVER)
                        .forEach(riverCard-> rivers.putIfAbsent(((FieldCard)riverCard).getGroupID(),((FieldCard)riverCard)));
        return rivers.values().stream().reduce(0,(subtotal,river)->subtotal+river.getFieldSize(),Integer::sum);
    }
}
