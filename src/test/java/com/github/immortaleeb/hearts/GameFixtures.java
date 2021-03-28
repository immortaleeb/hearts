package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.immortaleeb.hearts.CardFixtures.allCardsOfSuite;

public class GameFixtures {

    public static Map<PlayerId, List<Card>> fixedPlayerHands(List<PlayerId> players) {
        Map<PlayerId, List<Card>> fixedHands = new HashMap<>();

        fixedHands.put(players.get(0), allCardsOfSuite(Suite.HEARTS));
        fixedHands.put(players.get(1), allCardsOfSuite(Suite.CLUBS));
        fixedHands.put(players.get(2), allCardsOfSuite(Suite.DIAMONDS));
        fixedHands.put(players.get(3), allCardsOfSuite(Suite.SPADES));

        return fixedHands;
    }

}
