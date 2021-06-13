package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.util.Events;

public class RoundScenarioEvents {

    private final RoundScenario roundScenario;

    private RoundScenarioEvents(RoundScenario roundScenario) {
        this.roundScenario = roundScenario;
    }

    public Events eventsForAllTricks() {
        return eventsForFirst12Tricks()
            .addAll(trickEvents(13));
    }

    public Events eventsForFirst12Tricks() {
        Events events = Events.none();

        for (int i = 1; i <= 12; i++) {
            events.addAll(trickEvents(i));
        }

        return events;
    }

    public Events trickEvents(int trickNumber) {
        Trick trick = roundScenario.trick(trickNumber);
        return Events.of(trick.cardPlays())
            .add(trick.trickWon());
    }

    public static RoundScenarioEvents eventsFor(RoundScenario roundScenario) {
        return new RoundScenarioEvents(roundScenario);
    }

}
