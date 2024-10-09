package game.cards;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FieldCard extends BasicCard implements Card {

    private boolean calculated = false;

    public boolean isCalculated() {
        return calculated;
    }

    private Set<FieldCard> sameFieldCards = Set.of(this);
    private String groupID = UUID.randomUUID().toString();

    public Integer getFieldSize() {
        return sameFieldCards.size();
    }

    FieldCard(CardType type) {
        super(type);
    }

    public void setSameFieldCards(Set<FieldCard> sameFieldCards) {
        this.sameFieldCards = sameFieldCards;
    }
    public Set<FieldCard> setSameFieldCardsAndReturnIt(Set<FieldCard> sameFieldCards) {
        this.sameFieldCards = sameFieldCards;
        return this.sameFieldCards;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    protected Set<FieldCard> buildField(FieldCard prevCard) {
        this.setGroupID(prevCard.getGroupID());
        this.setSameFieldCards(Stream.concat(prevCard.sameFieldCards.stream(),this.sameFieldCards.stream()).collect(Collectors.toUnmodifiableSet()));
        List<FieldCard> neighbourFieldCards = getListOfUnvisitedNeighbours();

        if (neighbourFieldCards.isEmpty()) {
            return this.sameFieldCards;
        } else {
            return neighbourFieldCards.stream()
                    .map(card -> this.setSameFieldCardsAndReturnIt(
                            Stream.concat(this.sameFieldCards.stream(),card.buildField(this).stream())
                                    .collect(Collectors.toUnmodifiableSet())
                    )).flatMap(Collection::stream).collect(Collectors.toUnmodifiableSet());
        }
    }

    private List<FieldCard> getListOfUnvisitedNeighbours() {
        return getNeighbours().stream()
                .filter(card -> card.getType() == this.getType())
                .map(card -> (FieldCard) card)
                .filter(fieldCard -> !Objects.equals(fieldCard.getGroupID(), this.getGroupID()))
                .toList();
    }

    protected void propagateFieldLength(){
        this.sameFieldCards.forEach(fieldCard -> fieldCard.setSameFieldCards(this.sameFieldCards));
    }
    protected void propagateCalculated(){
        this.calculated=true;
        this.sameFieldCards.forEach(fieldCard -> fieldCard.calculated = true);
    }

    public void mergeFieldCards(){
        buildField(this);
        propagateFieldLength();
    }

}
