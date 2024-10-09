package game;

import game.board.Board;
import game.cards.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameTable {

    private final List<Player> players;


    public GameTable(List<Player> players) {
        this.players = players;
    }

    private void countPointsForGivenCardTypeInGeneralPointCount(Player player, Card card) {
        var cardPoints = card.count();
        player.getGeneralPointCount().put(card.getType(), player.getGeneralPointCount().get(card.getType()) + cardPoints);
    }

    public List<Player> endGame() {
        players.forEach(player -> {
            countAllCardPointsAndPutThemToGeneralPointCount(player);
        });
        compareWolvesAndAssignPoints();
        compareRiversAndAssignPoints();
        return players.stream().peek(player -> {
            countEcosystemGapsPoints(player);
            sumAllPoints(player);
        }).toList();
    }

    public void countAllCardPointsAndPutThemToGeneralPointCount(Player player) {
        for (var card : player.getBoard().getCardBoard().stream()
                .flatMap(Collection::stream).toList()) {
            switch (card.getType()) {
                case RIVER:
                    player.getBoard().setMaxRiverLength(Math.max(player.getBoard().getMaxRiverLength(), ((FieldCard) card).getFieldSize()));
                    break;
                case WOLF:
                    player.getBoard().setWolfCount(player.getBoard().getWolfCount() + 1);
                    break;
                case ELK:
                    break;
                default:
                    countPointsForGivenCardTypeInGeneralPointCount(player, card);
            }
        }
        countElkPoints(player);
    }

    private void countElkPoints(Player player) {
        var rowsWithAtLeast1Elk = (int) player.getBoard().getCardBoard().stream()
                .filter(row -> row.stream()
                        .anyMatch(card -> card.getType() == Card.CardType.ELK)
                ).count();
        var columnsWithAtLeast1Elk = 0;
        for (var y = 0; y < player.getBoard().getSizeHorizontal(); y++) {
            if (getColumn(player.getBoard(), y).stream().anyMatch(card -> card.getType() == Card.CardType.ELK)) {
                columnsWithAtLeast1Elk++;
            }
        }
        player.getGeneralPointCount().put(Card.CardType.ELK, (rowsWithAtLeast1Elk + columnsWithAtLeast1Elk) * ElkCard.POINTS_PER_VALID);
    }

    private List<Card> getColumn(Board board, int coordY) {
        return board.getCardBoard().stream().map(row -> row.get(coordY)).toList();
    }

    private void compareRiversAndAssignPoints() {
        var awardedPlayers = getRewardedPointsForRankedCard(Card.CardType.RIVER, 2);
        awardedPlayers.getOrDefault(0, List.of()).forEach(player -> player.getGeneralPointCount().put(Card.CardType.RIVER, player.getBoard().getMaxRiverLength() > 0 ? RiverCard.RIVER_1ST_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(1, List.of()).forEach(player -> player.getGeneralPointCount().put(Card.CardType.RIVER, player.getBoard().getMaxRiverLength() > 0 ? RiverCard.RIVER_2ND_PLACE_POINTS : 0));

    }

    private void compareWolvesAndAssignPoints() {
        var awardedPlayers = getRewardedPointsForRankedCard(Card.CardType.WOLF, 3);
        awardedPlayers.getOrDefault(0, List.of()).forEach(player -> player.getGeneralPointCount().put(Card.CardType.WOLF, player.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_1ST_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(1, List.of()).forEach(player -> player.getGeneralPointCount().put(Card.CardType.WOLF, player.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_2ND_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(2, List.of()).forEach(player -> player.getGeneralPointCount().put(Card.CardType.WOLF, player.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_3RD_PLACE_POINTS : 0));
    }

    private Map<Integer, List<Player>> getRewardedPointsForRankedCard(Card.CardType cardType, Integer podiumPLaces) {
        var playersGroupedByNumberOfRankedCards = players.stream()
                .sorted(Comparator.comparing(player -> getWolfOrRiverCount(player, cardType), Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(player -> getWolfOrRiverCount(player, cardType),LinkedHashMap::new,Collectors.toList()));

        var podium = new HashMap<Integer, List<Player>>();
        playersGroupedByNumberOfRankedCards.values().forEach(playersGroupedByCardCount -> {
            if (podium.values().stream().flatMap(Collection::stream).toList().size() < podiumPLaces) {
                podium.put(podium.size(), playersGroupedByCardCount);
            }
        });
        return Map.copyOf(podium);
    }

    private int getWolfOrRiverCount(Player player, Card.CardType type) {
        if (type == Card.CardType.RIVER) {
            return player.getBoard().getMaxRiverLength();
        } else if (type == Card.CardType.WOLF) {
            return player.getBoard().getWolfCount();
        } else {
            return 0;
        }

    }

    private void countEcosystemGapsPoints(Player player) {
        player.setNumberOfGaps((int) player.getGeneralPointCount().values().stream().filter(pointsPerType -> pointsPerType == 0).count());
        switch (player.getNumberOfGaps()) {
            case 0, 1, 2 -> player.setEcosystemGapPoints(12);
            case 3 -> player.setEcosystemGapPoints(7);
            case 4 -> player.setEcosystemGapPoints(3);
            case 5 -> player.setEcosystemGapPoints(0);
            default -> player.setEcosystemGapPoints(-5);
        }
    }

    private void sumAllPoints(Player player) {
        player.setSumOfPoints(
                player.getGeneralPointCount().values().stream().reduce(0, Integer::sum)
                        + player.getEcosystemGapPoints()
        );
    }

}
