package com.github.immortaleeb.hearts;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;
import static com.github.immortaleeb.hearts.scenarios.RoundScenarioEvents.eventsFor;

import com.github.immortaleeb.hearts.scenarios.RegularRound1Scenario;
import com.github.immortaleeb.hearts.scenarios.RegularRound4Scenario;
import com.github.immortaleeb.hearts.scenarios.RoundScenario;
import com.github.immortaleeb.hearts.scenarios.ShootForTheMoonRound1Scenario;
import com.github.immortaleeb.hearts.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ScenarioFixtures {

    public static Events gameStartedWith(List<PlayerId> players) {
        return Events.of(
                new GameStarted(gameId(), players),
                new CardsDealt(fixedPlayerHands(players))
        );
    }

    public static Events startedPlayingCardsWith(List<PlayerId> players) {
        return new Events()
                .addAll(gameStartedWith(players))
                .addAll(cardsPassedFor(players));
    }

    public static Events playedRoundsWith(int numberOfRounds, List<PlayerId> players) {
        Events events = Events.of(new GameStarted(gameId(), players));

        for (int roundIndex = 0; roundIndex < numberOfRounds; roundIndex++) {
            events.addAll(playedRoundWith(players, roundIndex));
        }

        return events.add(new CardsDealt(fixedPlayerHands(players)));
    }

    public static Events playRegular12Tricks(List<PlayerId> players) {
        return eventsFor(new RegularRound1Scenario(players)).eventsForFirst12Tricks();
    }

    public static Events play12TricksAnd3CardsofShootForTheMoonRound(List<PlayerId> players) {
        return eventsFor(new ShootForTheMoonRound1Scenario(players))
            .eventsForFirst12Tricks()
            .addAll(
                new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.TEN), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0))
            );
    }

    // helper methods

    private static Events playedRoundWith(List<PlayerId> players, int roundIndex) {
        Events events = Events.of(new CardsDealt(fixedPlayerHands(players)));

        if (shouldPassCardsThisRound(roundIndex)) {
            events.addAll(cardsPassedFor(players));
        }

        events.addAll(eventsFor(regularScenarioForRound(roundIndex).apply(players))
                .eventsForAllTricks());

        events.add(roundEnded(players, roundIndex));

        return events;
    }

    private static boolean shouldPassCardsThisRound(int roundIndex) {
        return normalizeRound(roundIndex + 1) != 0;
    }

    private static int normalizeRound(int roundIndex) {
        return roundIndex % 4;
    }

    private static RoundEnded roundEnded(List<PlayerId> players, int roundIndex) {
        return new RoundEnded(new HashMap<>() {{
            put(players.get(0), 0);
            put(players.get(1), 25 * (roundIndex + 1));
            put(players.get(2), roundIndex + 1);
            put(players.get(3), 0);
        }});
    }

    private static Function<List<PlayerId>, RoundScenario> regularScenarioForRound(int roundIndex) {
        return switch (normalizeRound(roundIndex)) {
            case 0, 1, 2 -> RegularRound1Scenario::new;
            case 3 -> RegularRound4Scenario::new;
            default -> throw new RuntimeException("illegal case");
        };
    }

    private static Events cardsPassedFor(List<PlayerId> players) {
        return Events.of(
                new PlayerPassedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerPassedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerPassedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerPassedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),
                new PlayerHasTakenPassedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerHasTakenPassedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerHasTakenPassedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerHasTakenPassedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),

                new StartedPlaying(players.get(1))
        );
    }

    private static GameId gameId() {
        return GameId.generate();
    }

}
