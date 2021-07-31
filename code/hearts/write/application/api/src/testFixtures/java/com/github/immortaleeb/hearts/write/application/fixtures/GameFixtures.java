package com.github.immortaleeb.hearts.write.application.fixtures;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.Suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFixtures {

    public static Map<PlayerId, List<Card>> fixedPlayerHands(List<PlayerId> players) {
        Map<PlayerId, List<Card>> fixedHands = new HashMap<>();

        fixedHands.put(players.get(0), CardFixtures.allCardsOfSuite(Suite.HEARTS));
        fixedHands.put(players.get(1), CardFixtures.allCardsOfSuite(Suite.CLUBS));
        fixedHands.put(players.get(2), CardFixtures.allCardsOfSuite(Suite.DIAMONDS));
        fixedHands.put(players.get(3), CardFixtures.allCardsOfSuite(Suite.SPADES));

        return fixedHands;
    }

}
