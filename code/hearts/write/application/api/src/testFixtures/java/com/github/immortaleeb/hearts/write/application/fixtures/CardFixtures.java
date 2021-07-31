package com.github.immortaleeb.hearts.write.application.fixtures;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.Rank;
import com.github.immortaleeb.hears.common.shared.Suite;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardFixtures {

    private static final Predicate<Card> NO_CARD = card -> false;

    public static Card twoOfClubs() {
        return Card.of(Suite.CLUBS, Rank.TWO);
    }

    public static List<Card> allCardsOfSuite(Suite suite) {
        return allCardsOfSuiteExcept(suite, NO_CARD);
    }

    public static List<Card> threeCardsOfSuite(Suite suite) {
        return Arrays.asList(
                Card.of(suite, Rank.TEN),
                Card.of(suite, Rank.JACK),
                Card.of(suite, Rank.ACE)
        );
    }

    public static List<Card> allCardsOfSuiteExcept(Suite suite, Predicate<Card> predicate) {
        return Arrays.stream(Rank.values())
            .map(rank -> new Card(suite, rank))
            .filter(predicate.negate())
            .collect(Collectors.toList());
    }
}
