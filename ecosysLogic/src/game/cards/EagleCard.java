package game.cards;

import java.util.HashSet;
import java.util.Set;

public class EagleCard extends BasicCard implements Card  {
    public static final Integer POINTS_PER_VALID=2;
    public EagleCard() {
        super(CardType.EAGLE);
    }

    @Override
    public Integer count() {
        return getFishAndRabbitsInRange2().size()*POINTS_PER_VALID;
    }

    private Set<Card> getFishAndRabbitsInRange2(){
        Set<Card> cards = new HashSet<>();
        if(getUpperNeighbour()!=null){
            checkCardsAboveAndAddThemToSet(cards);
        }
        if(getBottomNeighbour()!=null){
            checkCardsBelowAndAddThemToSet(cards);
        }
        if(getLeftNeighbour()!=null){
            checkCardsToTheLeftAndAddThemToSet(cards);
        }
        if(getRightNeighbour()!=null){
            checkCardsToTheRightAndAddThemToSet(cards);
        }
        return cards;
    }

    private void checkCardsToTheRightAndAddThemToSet(Set<Card> cards) {
        if(isFishOrRabbit(getRightNeighbour())){
            cards.add(getRightNeighbour());
        }
        if(isFishOrRabbit(getRightNeighbour().getRightNeighbour())){
            cards.add(getRightNeighbour().getRightNeighbour());
        }
        if(isFishOrRabbit(getRightNeighbour().getBottomNeighbour())){
            cards.add(getRightNeighbour().getBottomNeighbour());
        }
        if(isFishOrRabbit(getRightNeighbour().getUpperNeighbour())){
            cards.add(getRightNeighbour().getUpperNeighbour());
        }
    }
    private void checkCardsToTheLeftAndAddThemToSet(Set<Card> cards) {
        if(isFishOrRabbit(getLeftNeighbour())){
            cards.add(getLeftNeighbour());
        }
        if(isFishOrRabbit(getLeftNeighbour().getLeftNeighbour())){
            cards.add(getLeftNeighbour().getLeftNeighbour());
        }
        if(isFishOrRabbit(getLeftNeighbour().getBottomNeighbour())){
            cards.add(getLeftNeighbour().getBottomNeighbour());
        }
        if(isFishOrRabbit(getLeftNeighbour().getUpperNeighbour())){
            cards.add(getLeftNeighbour().getUpperNeighbour());
        }
    }
    private void checkCardsBelowAndAddThemToSet(Set<Card> cards) {
        if(isFishOrRabbit(getBottomNeighbour())){
            cards.add(getBottomNeighbour());
        }
        if(isFishOrRabbit(getBottomNeighbour().getBottomNeighbour())){
            cards.add(getBottomNeighbour().getBottomNeighbour());
        }
        if(isFishOrRabbit(getBottomNeighbour().getLeftNeighbour())){
            cards.add(getBottomNeighbour().getLeftNeighbour());
        }
        if(isFishOrRabbit(getBottomNeighbour().getRightNeighbour())){
            cards.add(getBottomNeighbour().getRightNeighbour());
        }
    }

    private void checkCardsAboveAndAddThemToSet(Set<Card> cards) {
        if(isFishOrRabbit(getUpperNeighbour())){
            cards.add(getUpperNeighbour());
        }
        if(isFishOrRabbit(getUpperNeighbour().getUpperNeighbour())){
            cards.add(getUpperNeighbour().getUpperNeighbour());
        }
        if(isFishOrRabbit(getUpperNeighbour().getLeftNeighbour())){
            cards.add(getUpperNeighbour().getLeftNeighbour());
        }
        if(isFishOrRabbit(getUpperNeighbour().getRightNeighbour())){
            cards.add(getUpperNeighbour().getRightNeighbour());
        }
    }


    private Boolean isFishOrRabbit(Card card){
        return  card!=null && (card.getType()==CardType.FISH || card.getType()==CardType.RABBIT);
    }
}
