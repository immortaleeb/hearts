package com.github.immortaleeb.hearts;

import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;
import static com.github.immortaleeb.hearts.scenarios.RoundScenarioEvents.eventsFor;

import com.github.immortaleeb.hearts.scenarios.RegularRound1Scenario;
import com.github.immortaleeb.hearts.scenarios.RegularRound4Scenario;
import com.github.immortaleeb.hearts.scenarios.RoundScenario;
import com.github.immortaleeb.hearts.scenarios.RoundScenarioEvents;
import com.github.immortaleeb.hearts.scenarios.ShootForTheMoonRound1Scenario;
import com.github.immortaleeb.hearts.util.Events;
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
                new CardsDealt(fixedPlayerHands(players))
        );
    }

    public static Events startedPlayingCardsWith(List<PlayerId> players) {
        return new Events()
                .addAll(gameStartedWith(players))
                .addAll(eventsFor(new RegularRound1Scenario(players)).eventsForCardsPassed());
    }

    public static Events playedRoundsWith(int numberOfRounds, List<PlayerId> players) {
        Events events = Events.of(new GameStarted(gameId(), players));

        for (int roundIndex = 0; roundIndex < numberOfRounds; roundIndex++) {
            RoundScenarioEvents scenarioEvents = eventsFor(regularScenarioForRound(roundIndex).apply(players));
            events.addAll(scenarioEvents.allEvents());
        }

        return events.add(new CardsDealt(fixedPlayerHands(players)));
    }

    public static Events playRegular12Tricks(List<PlayerId> players) {
        return eventsFor(new RegularRound1Scenario(players)).eventsForFirst12Tricks();
    }

    public static Events play12TricksAnd3CardsofShootForTheMoonRound(List<PlayerId> players) {
        RoundScenarioEvents scenarioEvents = eventsFor(new ShootForTheMoonRound1Scenario(players));

        return scenarioEvents
            .eventsForFirst12Tricks()
            .addAll(scenarioEvents.eventsForFirst3CardsOfTrick(13));
    }

    // helper methods

    private static Function<List<PlayerId>, RoundScenario> regularScenarioForRound(int roundIndex) {
        return switch (roundIndex % 4) {
            case 0, 1, 2 -> RegularRound1Scenario::new;
            case 3 -> RegularRound4Scenario::new;
            default -> throw new RuntimeException("illegal case");
        };
    }

    private static GameId gameId() {
        return GameId.generate();
    }

}
