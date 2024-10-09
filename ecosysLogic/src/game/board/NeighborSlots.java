package game.board;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class NeighborSlots {
    Slot topNeighbour;
    Slot bottomNeighbour;
    Slot leftNeighbour;
    Slot rightNeighbour;

    public List<Slot> toList(){
        return Stream.of(topNeighbour,bottomNeighbour,leftNeighbour,rightNeighbour).filter(Objects::nonNull).toList();
    }
    public Slot getTopNeighbour() {
        return topNeighbour;
    }

    public void setTopNeighbour(Slot topNeighbour) {
        this.topNeighbour = topNeighbour;
    }

    public Slot getBottomNeighbour() {
        return bottomNeighbour;
    }

    public void setBottomNeighbour(Slot bottomNeighbour) {
        this.bottomNeighbour = bottomNeighbour;
    }

    public Slot getLeftNeighbour() {
        return leftNeighbour;
    }

    public void setLeftNeighbour(Slot leftNeighbour) {
        this.leftNeighbour = leftNeighbour;
    }

    public Slot getRightNeighbour() {
        return rightNeighbour;
    }

    public void setRightNeighbour(Slot rightNeighbour) {
        this.rightNeighbour = rightNeighbour;
    }
}
