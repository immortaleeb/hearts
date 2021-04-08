package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardNotInHand;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.IncorrectNumberOfCardsPassed;
import com.github.immortaleeb.hearts.shared.InvalidCardPlayed;
import com.github.immortaleeb.hearts.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.shared.PlayerAlreadyPassedCards;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Game {

    private static final int CARDS_PER_HAND = 13;
    private static final int CARDS_TO_PASS = 3;
    private static final Card OPENING_CARD = Card.of(Suite.CLUBS, Rank.TWO);
    private static final int TRICKS_PER_ROUND = 13;
    private static final int SHOOT_FOR_THE_MOON_SCORE = 26;

    private final GameId id;
    private Players players;
    private final Map<PlayerId, List<Card>> cardsToReceive = new HashMap<>();
    private PlayerId leadingPlayer;
    private Trick trick = Trick.empty();
    private int tricksPlayed = 0;
    private Map<PlayerId, Integer> scores = new HashMap<>();
    private int roundNumber = 0;
    private GamePhase gamePhase = GamePhase.DEALING;

    private final List<GameEvent> raisedEvents = new ArrayList<>();

    public Game(GameId id) {
        this.id = id;
    }

    public GameId id() {
        return id;
    }

    private void dealCards() {
        Map<PlayerId, List<Card>> playerHands = dealHands();
        applyNewEvent(new CardsDealt(playerHands));

        if (!shouldPassCardsThisRounds()) {
            startPlaying();
        }
    }

    private Map<PlayerId, List<Card>> dealHands() {
        Map<PlayerId, List<Card>> playerHands = new HashMap<>();

        Deck deck = Deck.standard().shuffle();

        for (PlayerId player : this.players.toIds()) {
            playerHands.put(player, deck.takeCards(CARDS_PER_HAND));
        }

        return playerHands;
    }

    public void passCards(PlayerId fromPlayer, List<Card> cards) {
        if (cards.size() != CARDS_TO_PASS) {
            throw new IncorrectNumberOfCardsPassed();
        }

        if (!players.hasCards(fromPlayer, cards)) {
            throw new CardsNotInHand();
        }

        if (players.hasPassedCards(fromPlayer)) {
            throw new PlayerAlreadyPassedCards();
        }

        if (!shouldPassCardsThisRounds()) {
            throw new PlayerAlreadyPassedCards();
        }

        PlayerId playerToPassTo = choosePlayerToPassTo(fromPlayer);
        PlayerId playerToReceiveFrom = choosePlayerToReceiveFrom(fromPlayer);

        applyNewEvent(new PlayerPassedCards(fromPlayer, playerToPassTo, cards));

        receiveCards(fromPlayer, playerToPassTo, playerToReceiveFrom);

        if (players.allReceivedCards()) {
            startPlaying();
        }
    }

    private void receiveCards(PlayerId fromPlayer, PlayerId playerToPassTo, PlayerId playerToReceiveFrom) {
        if (players.hasPassedCards(playerToPassTo) && !players.hasReceivedCards(playerToPassTo)) {
            receiveCards(playerToPassTo);
        }

        if (players.hasPassedCards(playerToReceiveFrom)) {
            receiveCards(fromPlayer);
        }
    }

    public void playCard(PlayerId player, Card card) {
        if (!gamePhase.equals(GamePhase.PLAYING)) {
            throw new NotPlayersTurn();
        }

        boolean isPlayersTurn = leadingPlayer.equals(player) && !trick.hasPlayedCard(player);

        if (!isPlayersTurn) {
            throw new NotPlayersTurn();
        }

        if (!players.hasCard(player, card)) {
            throw new CardNotInHand();
        }

        Optional<String> validationError = validateCardPlay(player, card);
        if (validationError.isPresent()) {
            throw new InvalidCardPlayed(validationError.get());
        }

        PlayerId nextLeadingPlayer = chooseNextLeadingPlayer(player);

        applyNewEvent(new CardPlayed(player, card, nextLeadingPlayer));

        if (trick.numberOfPlayedCards() == players.size()) {
            applyNewEvent(new TrickWon(trick.decideWinner()));
        }

        if (tricksPlayed == TRICKS_PER_ROUND) {
            applyNewEvent(new RoundEnded(countRoundScores()));
            dealCards();
        }
    }

    private Optional<String> validateCardPlay(PlayerId player, Card card) {
        if (players.hasCard(player, OPENING_CARD) && !OPENING_CARD.equals(card)) {
            return Optional.of("You must open with the two of clubs");
        }

        if (!trick.isEmpty()) {
            Suite trickSuite = trick.suite();

            boolean playerCanFollowSuite = players.anyCard(player, Card.matchingSuite(trickSuite));
            boolean playerFollowsSuite = trickSuite == card.suite();
            boolean isValidPlay = playerFollowsSuite || !playerCanFollowSuite;

            return isValidPlay ? Optional.empty() : Optional.of("Player must follow suite when possible");
        }

        return Optional.empty();
    }

    private PlayerId chooseNextLeadingPlayer(PlayerId player) {
        return players.choosePlayerWithOffset(player, 1);
    }

    private PlayerId choosePlayerToPassTo(PlayerId fromPlayer) {
        return players.choosePlayerWithOffset(fromPlayer, choosePassingDirection().get().toOffset());
    }

    private PlayerId choosePlayerToReceiveFrom(PlayerId toPlayer) {
        return players.choosePlayerWithOffset(toPlayer, choosePassingDirection().get().fromOffset());
    }

    private boolean shouldPassCardsThisRounds() {
        return choosePassingDirection().isPresent();
    }

    private Optional<PassDirection> choosePassingDirection() {
        return Optional.ofNullable(switch (roundNumber % 4) {
            case 1 -> PassDirection.LEFT;
            case 2 -> PassDirection.RIGHT;
            case 3 -> PassDirection.DIAGONAL;
            default -> null;
        });
    }

    public void applyNewEvent(GameEvent event) {
        apply(event);
        raiseEvent(event);
    }

    public void loadFromHistory(List<GameEvent> events) {
        for (GameEvent event : events) {
            System.out.println(event);
            apply(event);
        }
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
        } else if (event instanceof StartedPlaying) {
            applyEvent((StartedPlaying) event);
        } else if (event instanceof CardPlayed) {
            applyEvent((CardPlayed) event);
        } else if (event instanceof TrickWon) {
            applyEvent((TrickWon) event);
        } else if (event instanceof RoundEnded) {
            applyEvent((RoundEnded) event);
        } else {
            throw new RuntimeException("Unknown event type " + event.getClass());
        }
    }

    public void applyEvent(GameStarted event) {
        players = Players.of(event.players());
        initializeScores(event.players());
    }

    private void initializeScores(List<PlayerId> playerIds) {
        scores = new HashMap<>();
        for (PlayerId player : playerIds) {
            scores.put(player, 0);
        }
    }

    public void applyEvent(CardsDealt event) {
        players.dealCards(event.playerHands());
        roundNumber++;
    }

    public void applyEvent(PlayerPassedCards event) {
        players.markCardsPassed(event.fromPlayer());
        players.takeCards(event.fromPlayer(), event.passedCards());
        cardsToReceive.put(event.toPlayer(), event.passedCards());
    }

    private void receiveCards(PlayerId toPlayer) {
        PlayerId fromPlayer = choosePlayerToReceiveFrom(toPlayer);
        applyNewEvent(new PlayerReceivedCards(fromPlayer, toPlayer, cardsToReceive.get(toPlayer)));
    }

    public void applyEvent(PlayerReceivedCards event) {
        players.markCardsReceived(event.toPlayer());
        players.giveCards(event.toPlayer(), event.cards());
    }

    private void startPlaying() {
        PlayerId leadingPlayer = players.getPlayerWithCard(OPENING_CARD);
        applyNewEvent(new StartedPlaying(leadingPlayer));
    }

    public void applyEvent(StartedPlaying event) {
        leadingPlayer = event.leadingPlayer();
        gamePhase = GamePhase.PLAYING;
    }

    private void applyEvent(CardPlayed event) {
        trick.play(event.card(), event.playedBy());
        leadingPlayer = event.nextLeadingPlayer();
        players.takeCard(event.playedBy(), event.card());
    }

    private void applyEvent(TrickWon event) {
        countScores(trick);

        leadingPlayer = event.wonBy();
        trick = Trick.empty();
        tricksPlayed++;
    }

    private void countScores(Trick trick) {
        int trickScore = trick.calculateScore();
        scores.compute(trick.decideWinner(), (k, score) -> score + trickScore);
    }

    private Map<PlayerId, Integer> countRoundScores() {
        Optional<PlayerId> playerWhoShotForTheMoon = scores.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == SHOOT_FOR_THE_MOON_SCORE)
                .map(Map.Entry::getKey)
                .findFirst();

        playerWhoShotForTheMoon.ifPresent(playerId -> scores.compute(playerId, (k, score) -> -score));

        return scores;
    }

    private void applyEvent(RoundEnded event) {
        // nothing to do
    }

    public static Game startWith(List<PlayerId> players) {
        Game game = new Game(GameId.generate());
        game.applyNewEvent(new GameStarted(players));

        game.dealCards();

        return game;
    }

    public List<GameEvent> raisedEvents() {
        return raisedEvents;
    }

    private void raiseEvent(GameEvent event) {
        this.raisedEvents.add(event);
    }

    public void clearRaisedEvents() {
        raisedEvents.clear();
    }
}
