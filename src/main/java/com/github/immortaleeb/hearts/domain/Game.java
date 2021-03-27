package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.IncorrectNumberOfCardsPassed;
import com.github.immortaleeb.hearts.shared.PlayerAlreadyPassedCards;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private static final int CARDS_PER_HAND = 13;
    private static final int CARDS_TO_PASS = 3;

    private final GameId id;
    private Players players;
    private final Map<PlayerId, List<Card>> cardsToReceive = new HashMap<>();

    private final List<GameEvent> raisedEvents = new ArrayList<>();

    public Game(GameId id) {
        this.id = id;
    }

    public GameId id() {
        return id;
    }

    private void dealCardsFor(List<PlayerId> players) {
        Map<PlayerId, List<Card>> playerHands = dealHands(players);
        applyNewEvent(new CardsDealt(playerHands));
    }

    private Map<PlayerId, List<Card>> dealHands(List<PlayerId> players) {
        Map<PlayerId, List<Card>> playerHands = new HashMap<>();

        Deck deck = Deck.standard().shuffle();

        for (PlayerId player : players) {
            playerHands.put(player, deck.takeCards(CARDS_PER_HAND));
        }

        return playerHands;
    }

    public void passCards(PlayerId fromPlayer, List<Card> cards) {
        if (cards.size() != CARDS_TO_PASS) {
            throw new IncorrectNumberOfCardsPassed();
        }

        List<Card> hand = players.handOf(fromPlayer);

        if (!hand.containsAll(cards)) {
            throw new CardsNotInHand();
        }

        if (players.hasPassedCards(fromPlayer)) {
            throw new PlayerAlreadyPassedCards();
        }

        PlayerId toPlayer = choosePlayerToPassTo(fromPlayer);

        applyNewEvent(new PlayerPassedCards(fromPlayer, toPlayer, cards));
    }

    private PlayerId choosePlayerToPassTo(PlayerId fromPlayer) {
        return players.choosePlayerWithOffset(fromPlayer, 1);
    }

    private PlayerId choosePlayerToReceiveFrom(PlayerId toPlayer) {
        return players.choosePlayerWithOffset(toPlayer, -1);
    }

    public void applyNewEvent(GameEvent event) {
        apply(event);
        raiseEvent(event);
    }

    public void apply(GameEvent event) {
        if (event instanceof GameStarted) {
            applyEvent((GameStarted) event);
        } else if (event instanceof CardsDealt) {
            applyEvent((CardsDealt) event);
        } else if (event instanceof PlayerPassedCards) {
            applyEvent((PlayerPassedCards) event);
        } else if (event instanceof PlayerReceivedCards) {
            applyEvent((PlayerReceivedCards) event);
        } else if (event instanceof RoundStarted) {
            applyEvent((RoundStarted) event);
        } else {
            throw new RuntimeException("Unknown event type " + event.getClass());
        }
    }

    public void applyEvent(GameStarted event) {
        players = Players.of(event.players());
    }

    public void applyEvent(CardsDealt event) {
        players.dealCards(event.playerHands());
    }

    public void applyEvent(PlayerPassedCards event) {
        PlayerId currentPlayer = event.fromPlayer();
        PlayerId playerToReceiveFrom = choosePlayerToReceiveFrom(currentPlayer);
        PlayerId playerToPassTo = event.toPlayer();

        players.markCardsPassed(currentPlayer);
        cardsToReceive.put(playerToPassTo, event.passedCards());

        if (players.hasPassedCards(playerToPassTo)) {
            receiveCards(playerToPassTo);
        }

        if (players.hasPassedCards(playerToReceiveFrom)) {
            receiveCards(currentPlayer);
        }
    }

    private void receiveCards(PlayerId toPlayer) {
        PlayerId fromPlayer = choosePlayerToReceiveFrom(toPlayer);
        applyNewEvent(new PlayerReceivedCards(fromPlayer, toPlayer, cardsToReceive.get(toPlayer)));
    }

    public void applyEvent(PlayerReceivedCards event) {
        players.markCardsReceived(event.fromPlayer());

        if (players.allReceivedCards()) {
            applyNewEvent(new RoundStarted(players.get(0).id()));
        }
    }

    private void applyEvent(RoundStarted event) {

    }

    public static Game startWith(List<PlayerId> players) {
        Game game = new Game(GameId.generate());
        game.applyNewEvent(new GameStarted(players));

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
