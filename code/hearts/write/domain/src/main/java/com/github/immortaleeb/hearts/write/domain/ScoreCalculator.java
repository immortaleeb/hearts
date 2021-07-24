package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ScoreCalculator {

    private final int shootForTheMoonScore;

    public ScoreCalculator(int shootForTheMoonScore) {
        this.shootForTheMoonScore = shootForTheMoonScore;
    }

    public Map<PlayerId, Integer> countRoundScores(Map<PlayerId, List<Trick>> wonTricks) {
        return wonTricks
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> calculateScore(entry.getValue())));
    }

    private int calculateScore(List<Trick> tricks) {
        int roundScore = tricks.stream()
                .map(Trick::cards)
                .mapToInt(this::calculateScore)
                .sum();

        if (roundScore == shootForTheMoonScore) {
            return -roundScore;
        }

        return roundScore;
    }

    private int calculateScore(Stream<Card> cards) {
        return cards.mapToInt(this::scoreOf).sum();
    }

    private int scoreOf(Card card) {
        if (card.matchesSuite(Suite.HEARTS)) {
            return 1;
        } else if (card.equals(Card.of(Suite.SPADES, Rank.QUEEN))) {
            return 13;
        }

        return 0;
    }

}
