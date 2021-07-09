package com.github.immortaleeb.hearts.write.scenarios;

import com.github.immortaleeb.hearts.write.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;

public class RoundScenarioEvents {

    private final RoundScenario roundScenario;

    private RoundScenarioEvents(RoundScenario roundScenario) {
        this.roundScenario = roundScenario;
    }

    public Events allEvents() {
        return Events.none()
            .addAll(eventsForCardsDealt())
            .addAll(eventsForCardsPassed())
            .addAll(eventsForAllTricks())
            .add(eventForRoundEnded());
    }

    public Events eventsForCardsDealt() {
        return Events.of(new CardsDealt(roundScenario.cardsDealt()));
    }

    public Events eventsForCardsPassed() {
        PassedCards passedCards = roundScenario.cardsPassed();

        Events events = Events.none();

        for (CardPass cardPass : passedCards.cardPasses()) {
            events.add(new PlayerPassedCards(cardPass.fromPlayer(), cardPass.toPlayer(), cardPass.cards()));
        }

        for (CardPass cardPass : passedCards.cardPasses()) {
            events.add(new PlayerHasTakenPassedCards(cardPass.fromPlayer(), cardPass.toPlayer(), cardPass.cards()));
        }

        return events.add(new StartedPlaying(roundScenario.leadingPlayer()));
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

    public Events eventsForFirst3CardsOfTrick(int trickNumber) {
        return Events.of(roundScenario.trick(trickNumber).cardPlays().subList(0, 3));
    }

    public Events trickEvents(int trickNumber) {
        Trick trick = roundScenario.trick(trickNumber);
        return Events.of(trick.cardPlays())
            .add(trick.trickWon());
    }

    private RoundEnded eventForRoundEnded() {
        return new RoundEnded(roundScenario.roundScore());
    }

    public static RoundScenarioEvents eventsFor(RoundScenario roundScenario) {
        return new RoundScenarioEvents(roundScenario);
    }

}
