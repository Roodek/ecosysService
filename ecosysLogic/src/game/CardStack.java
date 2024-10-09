package game;

import game.cards.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardStack {

    private static final int RIVER_MEADOW_COUNT = 20;
    private static final int ELK_BEAR_FOX_WOLF_COUNT = 12;
    private static final int FISH_COUNT = 10;
    private static final int DRAGONFLY_BEE_EAGLE_RABBIT_COUNT = 8;


    //meadows x20
    //rivers x20
    //elks x12
    //bears x12
    //fox x12
    //wolf x12
    //fish x10
    //dragonflies x8
    //bees x8
    //eagles x8
    //rabbits x8

    public static List<Card> initCardStack(){
        var cards = new LinkedList<Card>();
        for(int i=0;i<RIVER_MEADOW_COUNT;i++){
            cards.add(new MeadowCard());
            cards.add(new RiverCard());
            if(i<ELK_BEAR_FOX_WOLF_COUNT){
                cards.add(new ElkCard());
                cards.add(new BearCard());
                cards.add(new FoxCard());
                cards.add(new WolfCard());
            }
            if(i<FISH_COUNT){
                cards.add(new FishCard());
            }
            if(i<DRAGONFLY_BEE_EAGLE_RABBIT_COUNT){
                cards.add(new DragonflyCard());
                cards.add(new BeeCard());
                cards.add(new EagleCard());
                cards.add(new RabbitCard());
            }
        }
        Collections.shuffle(cards);
        return cards;
    }

    public static Card dealCard(List<Card> cards){
        var poppedCard = cards.get(0);
        cards.remove(cards.get(0));
        return poppedCard;
    }

    public static List<Card> dealFirstHandFromStack(List<Card> cards){
        var hand = List.copyOf(cards.subList(0,11));
        cards.removeAll(hand);
        return hand;
    }
    public static List<Card> dealSecondHandFromStack(List<Card> cards){
        var hand = List.copyOf(cards.subList(0,10));
        cards.removeAll(hand);
        return hand;
    }

}
