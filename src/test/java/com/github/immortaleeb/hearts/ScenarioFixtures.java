package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.domain.CardsDealt;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.domain.GameStarted;
import com.github.immortaleeb.hearts.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.domain.PlayerReceivedCards;
import com.github.immortaleeb.hearts.domain.RoundStarted;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.Arrays;
import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;

public class ScenarioFixtures {

    public static List<GameEvent> gameStartedWith(List<PlayerId> players) {
        return Arrays.asList(
                new GameStarted(players),
                new CardsDealt(fixedPlayerHands(players))
        );
    }

    public static List<GameEvent> round1StartedWith(List<PlayerId> players) {
        return Arrays.asList(
                new GameStarted(players),
                new CardsDealt(fixedPlayerHands(players)),
                new PlayerPassedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerPassedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerPassedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerPassedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),
                new PlayerReceivedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerReceivedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerReceivedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerReceivedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),
                new RoundStarted(players.get(1))
        );
    }

}
