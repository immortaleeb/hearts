package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private static final int CARDS_PER_HAND = 13;

    private final GameId id;
    private final List<GameEvent> raisedEvents = new ArrayList<>();

    private Game(GameId id) {
        this.id = id;
    }

    public GameId id() {
        return id;
    }

    private void dealCardsFor(List<PlayerId> players) {
        Map<PlayerId, List<Card>> playerHands = dealHands(players);
        raiseEvent(new CardsDealt(playerHands));
    }

    private Map<PlayerId, List<Card>> dealHands(List<PlayerId> players) {
        Map<PlayerId, List<Card>> playerHands = new HashMap<>();

        Deck deck = Deck.standard().shuffle();

        for (PlayerId player : players) {
            playerHands.put(player, deck.takeCards(CARDS_PER_HAND));
        }

        return playerHands;
    }

    public static Game startWith(List<PlayerId> players) {
        Game game = new Game(GameId.generate());
        game.raiseEvent(new GameStarted(players));

        game.dealCardsFor(players);

        return game;
    }

    public List<GameEvent> raisedEvents() {
        return raisedEvents;
    }

    private void raiseEvent(GameEvent event) {
        this.raisedEvents.add(event);
    }
}
