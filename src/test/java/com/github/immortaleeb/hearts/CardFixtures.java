package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardFixtures {

    public static Card twoOfClubs() {
        return Card.of(Suite.CLUBS, Rank.TWO);
    }

    public static List<Card> allCardsOfSuite(Suite suite) {
        return Arrays.stream(Rank.values())
                .map(rank -> new Card(suite, rank))
                .collect(Collectors.toList());
    }

    public static List<Card> threeCardsOfSuite(Suite suite) {
        return Arrays.asList(
                Card.of(suite, Rank.TEN),
                Card.of(suite, Rank.JACK),
                Card.of(suite, Rank.ACE)
        );
    }

}
