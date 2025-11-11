package com.eco.ecosystem.game;


import com.eco.ecosystem.game.board.Board;
import com.eco.ecosystem.game.cards.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static com.eco.ecosystem.game.cards.Card.CardType.*;

@Getter
public class GameTable {

    private final List<GamePlayer> gamePlayers;


    public GameTable(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    private void countPointsForGivenCardTypeInGeneralPointCount(GamePlayer gamePlayer, Card card) {
        var cardPoints = card.count();
        gamePlayer.getGeneralPointCount().put(card.getType(), gamePlayer.getGeneralPointCount().get(card.getType()) + cardPoints);
    }

    public List<GamePlayer> endGame() {
        gamePlayers.forEach(this::prepareCardPointsForPlayer);

        compareWolvesAndAssignPoints();
        compareRiversAndAssignPoints();

        gamePlayers.forEach(this::calculateGapsAndSumAllPoints);
        return gamePlayers;
    }

    private void calculateGapsAndSumAllPoints(GamePlayer gamePlayer) {
        countEcosystemGapsPoints(gamePlayer);
        sumAllPoints(gamePlayer);
    }

    private void prepareCardPointsForPlayer(GamePlayer gamePlayer) {
        gamePlayer.getBoard().establishCardRelationships();
        this.countAllCardPointsAndPutThemToGeneralPointCount(gamePlayer);
    }

    public void countAllCardPointsAndPutThemToGeneralPointCount(GamePlayer gamePlayer) {
        for (var card : gamePlayer.getBoard().getCardBoard().stream()
                .flatMap(Collection::stream).toList()) {
            switch (card.getType()) {
                case RIVER:
                    gamePlayer.getBoard().setMaxRiverLength(Math.max(gamePlayer.getBoard().getMaxRiverLength(), ((FieldCard) card).getFieldSize()));
                    break;
                case WOLF:
                    gamePlayer.getBoard().setWolfCount(gamePlayer.getBoard().getWolfCount() + 1);
                    break;
                case ELK:
                    break;
                default:
                    countPointsForGivenCardTypeInGeneralPointCount(gamePlayer, card);
            }
        }
        countElkPoints(gamePlayer);
    }

    private void countElkPoints(GamePlayer gamePlayer) {
        var rowsWithAtLeast1Elk = (int) gamePlayer.getBoard().getCardBoard().stream()
                .filter(row -> row.stream()
                        .anyMatch(card -> card.getType() == ELK)
                ).count();
        var columnsWithAtLeast1Elk = 0;
        for (var y = 0; y < gamePlayer.getBoard().getSizeHorizontal(); y++) {
            if (getColumn(gamePlayer.getBoard(), y).stream().anyMatch(card -> card.getType() == ELK)) {
                columnsWithAtLeast1Elk++;
            }
        }
        gamePlayer.getGeneralPointCount().put(ELK, (rowsWithAtLeast1Elk + columnsWithAtLeast1Elk) * ElkCard.POINTS_PER_VALID);
    }

    private List<Card> getColumn(Board board, int coordY) {
        return board.getCardBoard().stream().map(row -> row.get(coordY)).toList();
    }

    private void compareRiversAndAssignPoints() {
        var awardedPlayers = getRewardedPointsForRankedCard(RIVER, 2);
        awardedPlayers.getOrDefault(0, List.of()).forEach(gamePlayer -> gamePlayer.getGeneralPointCount().put(RIVER, gamePlayer.getBoard().getMaxRiverLength() > 0 ? RiverCard.RIVER_1ST_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(1, List.of()).forEach(gamePlayer -> gamePlayer.getGeneralPointCount().put(RIVER, gamePlayer.getBoard().getMaxRiverLength() > 0 ? RiverCard.RIVER_2ND_PLACE_POINTS : 0));

    }

    private void compareWolvesAndAssignPoints() {
        var awardedPlayers = getRewardedPointsForRankedCard(WOLF, 3);
        awardedPlayers.getOrDefault(0, List.of()).forEach(gamePlayer -> gamePlayer.getGeneralPointCount().put(WOLF, gamePlayer.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_1ST_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(1, List.of()).forEach(gamePlayer -> gamePlayer.getGeneralPointCount().put(WOLF, gamePlayer.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_2ND_PLACE_POINTS : 0));
        awardedPlayers.getOrDefault(2, List.of()).forEach(gamePlayer -> gamePlayer.getGeneralPointCount().put(WOLF, gamePlayer.getBoard().getWolfCount() > 0 ? WolfCard.WOLF_3RD_PLACE_POINTS : 0));
    }

    private Map<Integer, List<GamePlayer>> getRewardedPointsForRankedCard(Card.CardType cardType, Integer podiumPlaces) {
        var playersGroupedByNumberOfRankedCards = gamePlayers.stream()
                .sorted(Comparator.comparing(gamePlayer -> getWolfOrRiverCount(gamePlayer, cardType), Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(gamePlayer -> getWolfOrRiverCount(gamePlayer, cardType), LinkedHashMap::new, Collectors.toList()));

        var podium = new HashMap<Integer, List<GamePlayer>>();
        playersGroupedByNumberOfRankedCards.values().forEach(playersGroupedByCardCount -> {
            var podiumSize = podium.values().stream().flatMap(Collection::stream).toList().size();
            if (podiumSize < podiumPlaces) {
                podium.put(podiumSize, playersGroupedByCardCount);
            }
        });
        return Map.copyOf(podium);
    }

    private int getWolfOrRiverCount(GamePlayer gamePlayer, Card.CardType type) {
        if (type == RIVER) {
            return gamePlayer.getBoard().getMaxRiverLength();
        } else if (type == WOLF) {
            return gamePlayer.getBoard().getWolfCount();
        } else {
            return 0;
        }

    }

    private void countEcosystemGapsPoints(GamePlayer gamePlayer) {
        gamePlayer.setNumberOfGaps((int) gamePlayer.getGeneralPointCount().values().stream().filter(pointsPerType -> pointsPerType == 0).count());
        switch (gamePlayer.getNumberOfGaps()) {
            case 0, 1, 2 -> gamePlayer.setEcosystemGapPoints(12);
            case 3 -> gamePlayer.setEcosystemGapPoints(7);
            case 4 -> gamePlayer.setEcosystemGapPoints(3);
            case 5 -> gamePlayer.setEcosystemGapPoints(0);
            default -> gamePlayer.setEcosystemGapPoints(-5);
        }
    }

    private void sumAllPoints(GamePlayer gamePlayer) {
        gamePlayer.setSumOfPoints(
                gamePlayer.getGeneralPointCount().values().stream().reduce(0, Integer::sum)
                        + gamePlayer.getEcosystemGapPoints()
        );
    }

}
