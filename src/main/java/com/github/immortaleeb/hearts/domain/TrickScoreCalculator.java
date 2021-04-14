package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.stream.Stream;

class TrickScoreCalculator {

    public int calculateScore(Stream<Card> cards) {
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
