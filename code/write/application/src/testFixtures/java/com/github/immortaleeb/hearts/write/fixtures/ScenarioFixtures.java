package com.github.immortaleeb.hearts.write.fixtures;

import static com.github.immortaleeb.hearts.write.fixtures.GameFixtures.fixedPlayerHands;
import static com.github.immortaleeb.hearts.write.scenarios.RoundScenarioEvents.eventsFor;

import com.github.immortaleeb.hearts.write.scenarios.RegularRound1Scenario;
import com.github.immortaleeb.hearts.write.scenarios.RegularRound2Scenario;
import com.github.immortaleeb.hearts.write.scenarios.RegularRound3Scenario;
import com.github.immortaleeb.hearts.write.scenarios.RegularRound4Scenario;
import com.github.immortaleeb.hearts.write.scenarios.RoundScenario;
import com.github.immortaleeb.hearts.write.scenarios.RoundScenarioEvents;
import com.github.immortaleeb.hearts.write.scenarios.ShootForTheMoonRound1Scenario;
import com.github.immortaleeb.hearts.write.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;
import java.util.function.Function;

public class ScenarioFixtures {

    public static Events gameStartedWith(List<PlayerId> players) {
        return Events.of(
                new GameStarted(gameId(), players),
                new CardsDealt(gameId(), fixedPlayerHands(players))
        );
    }

    public static Events startedPlayingCardsWith(List<PlayerId> players) {
        return new Events()
                .addAll(gameStartedWith(players))
                .addAll(eventsFor(new RegularRound1Scenario(players)).eventsForCardsPassed(gameId()));
    }

    public static Events playedRoundsWith(int numberOfRounds, List<PlayerId> players) {
        GameId gameId = gameId();
        Events events = Events.of(new GameStarted(gameId, players));

        for (int roundIndex = 0; roundIndex < numberOfRounds; roundIndex++) {
            RoundScenarioEvents scenarioEvents = eventsFor(regularScenarioForRound(roundIndex).apply(players));
            events.addAll(scenarioEvents.allEvents(gameId));
        }

        RoundScenarioEvents nextRoundEvents = eventsFor(regularScenarioForRound(numberOfRounds).apply(players));

        return events.addAll(nextRoundEvents.eventsForCardsDealt(gameId));
    }

    public static Events playRegular12Tricks(List<PlayerId> players) {
        return eventsFor(regularScenarioForRound(0).apply(players)).eventsForFirst12Tricks();
    }

    public static Events play12TricksAnd3CardsofShootForTheMoonRound(List<PlayerId> players) {
        RoundScenarioEvents scenarioEvents = eventsFor(new ShootForTheMoonRound1Scenario(players));

        return scenarioEvents
            .eventsForFirst12Tricks()
            .addAll(scenarioEvents.eventsForFirst3CardsOfTrick(13));
    }

    public static Function<List<PlayerId>, RoundScenario> regularScenarioForRound(int roundIndex) {
        return switch (roundIndex % 4) {
            case 0 -> RegularRound1Scenario::new;
            case 1 -> RegularRound2Scenario::new;
            case 2 -> RegularRound3Scenario::new;
            case 3 -> RegularRound4Scenario::new;
            default -> throw new RuntimeException("illegal case");
        };
    }

    private static GameId gameId() {
        return GameId.generate();
    }

}
