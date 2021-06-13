package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.util.Events;

public interface RoundScenario {

    default Events eventsForAllTricks() {
        return eventsForFirst12Tricks()
            .addAll(trick(13));
    }

    default Events eventsForFirst12Tricks() {
        Events events = Events.none();

        for (int i = 1; i <= 12; i++) {
            events.addAll(trick(i));
        }

        return events;
    }

    Events trick(int trickNumber);

}
