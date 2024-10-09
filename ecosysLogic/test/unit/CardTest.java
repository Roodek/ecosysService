package unit;

import game.Player;
import game.board.AssignNeighboursToCardsStrategy;
import game.board.Board;
import game.cards.*;
import game.utils.BoardSlotProcessor;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CardTest {

    public Board board;
    @BeforeEach
    void initBoard(){
        this.board = new Board();

    }

    public void setBoard(ArrayList<ArrayList<Card>> board){
        for(var row:board){
            this.board.getCardBoard().add(row);
        }
        this.board.setSizeVertical(board.size());
        this.board.setSizeHorizontal(board.get(0).size());
    }
    public void setBoardAndAssignNeighbours(Player player, ArrayList<ArrayList<Card>> board){
        for(var row:board){
            player.getBoard().getCardBoard().add(row);
        }
        player.getBoard().setSizeVertical(board.size());
        player.getBoard().setSizeHorizontal(board.get(0).size());
        assignNeighbours(player.getBoard());
    }

    protected void assignNeighbours(Board board) {
        var assignNeighboursStrategy = new AssignNeighboursToCardsStrategy(board);
        BoardSlotProcessor.iterateOverBoardEntriesAndApplyStrategy(board.getCardBoard(), assignNeighboursStrategy);
        mergeRiversAndMeadows(board);

    }

    private void mergeRiversAndMeadows(Board board) {
        board.getCardBoard().stream()
                .flatMap(Collection::stream)
                .filter(card -> card.getType() == Card.CardType.RIVER || card.getType() == Card.CardType.MEADOW)
                .map(card -> (FieldCard) card)
                .forEach(FieldCard::mergeFieldCards);

    }

    void initializeBasicBoardWithRiverAnd2Meadows(){
        Card bear1 = new BearCard();
        Card bear2 = new BearCard();
        Card bear3 = new BearCard();
        Card bear4 = new BearCard();

        Card fox1 = new FoxCard();
        Card fish1 = new FishCard();
        Card fish2 = new FishCard();
        Card bee1 = new BeeCard();
        Card bee2 = new BeeCard();
        Card rabbit1 = new RabbitCard();
        Card rabbit2 = new RabbitCard();
        Card rabbit3 = new RabbitCard();
        Card rabbit4 = new RabbitCard();

        FieldCard river1 = new RiverCard();
        FieldCard river2 = new RiverCard();
        FieldCard river3 = new RiverCard();
        FieldCard river4 = new RiverCard();
        FieldCard river5 = new RiverCard();
        FieldCard river6 = new RiverCard();
        FieldCard meadow1 = new MeadowCard();
        FieldCard meadow2 = new MeadowCard();
        FieldCard meadow3 = new MeadowCard();
        FieldCard meadow4 = new MeadowCard();
        FieldCard meadow5 = new MeadowCard();
        FieldCard meadow6 = new MeadowCard();

        var exampleBoard = new ArrayList<ArrayList<Card>>();
        exampleBoard.add(new ArrayList<>(Arrays.asList(river1,river2,fish1 ,bee1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(meadow6  ,river3,river4,river5)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(fox1  ,bear2 ,river6 ,bee2)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(bear4  ,meadow5 ,bear3 ,meadow1)));
        exampleBoard.add(new ArrayList<>(Arrays.asList(rabbit1  ,meadow4 ,meadow3  ,meadow2)));
    }
}
