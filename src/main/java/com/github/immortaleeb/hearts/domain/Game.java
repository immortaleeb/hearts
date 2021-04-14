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

    private final Table table = new Table();
    private final Dealer dealer = new Dealer(CARDS_PER_HAND);
    private final TrickScoreCalculator scoreCalculator = new TrickScoreCalculator();

    private Players players;
    private PlayerId leadingPlayer;
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
        Map<PlayerId, List<Card>> playerHands = dealer.deal(players.ids());
        applyNewEvent(new CardsDealt(playerHands));

        if (!shouldPassCardsThisRounds()) {
            startPlaying();
        }
    }

    public void passCards(PlayerId fromPlayerId, List<Card> cards) {
        if (cards.size() != CARDS_TO_PASS) {
            throw new IncorrectNumberOfCardsPassed();
        }

        Player fromPlayer = players.getPlayerById(fromPlayerId);

        if (!fromPlayer.hand().contains(cards)) {
            throw new CardsNotInHand();
        }

        if (fromPlayer.hasPassedCards()) {
            throw new PlayerAlreadyPassedCards();
        }

        if (!shouldPassCardsThisRounds()) {
            throw new PlayerAlreadyPassedCards();
        }

        PlayerId playerToPassToId = choosePlayerToPassTo(fromPlayerId);

        applyNewEvent(new PlayerPassedCards(fromPlayerId, playerToPassToId, cards));

        takePassedCards(playerToPassToId);
        takePassedCards(fromPlayerId);

        if (allPlayersPassedCards()) {
            startPlaying();
        }
    }

    private boolean allPlayersPassedCards() {
        return players.all(Player::hasPassedCards);
    }

    public void playCard(PlayerId playerId, Card card) {
        if (!gamePhase.equals(GamePhase.PLAYING)) {
            throw new NotPlayersTurn();
        }

        boolean isPlayersTurn = leadingPlayer.equals(playerId) && !table.hasPlayedCard(playerId);

        if (!isPlayersTurn) {
            throw new NotPlayersTurn();
        }

        Player player = players.getPlayerById(playerId);

        if (!player.hand().contains(card)) {
            throw new CardNotInHand();
        }

        Optional<String> validationError = validateCardPlay(playerId, card);
        if (validationError.isPresent()) {
            throw new InvalidCardPlayed(validationError.get());
        }

        PlayerId nextLeadingPlayer = chooseNextLeadingPlayer(playerId);

        applyNewEvent(new CardPlayed(playerId, card, nextLeadingPlayer));

        if (table.numberOfPlayedCards() == players.size()) {
            applyNewEvent(new TrickWon(table.trick().winner()));
        }

        if (tricksPlayed == TRICKS_PER_ROUND) {
            applyNewEvent(new RoundEnded(countRoundScores()));
            dealCards();
        }
    }

    private Optional<String> validateCardPlay(PlayerId playerId, Card card) {
        Player player = players.getPlayerById(playerId);

        if (player.hand().contains(OPENING_CARD) && !OPENING_CARD.equals(card)) {
            return Optional.of("You must open with the two of clubs");
        }

        if (table.hasPlayedCards()) {
            Suite trickSuite = table.trick().suite();

            boolean playerCanFollowSuite = player.hand().anyCard(Card.matchingSuite(trickSuite));
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
        } else if (event instanceof PlayerHasTakenPassedCards) {
            applyEvent((PlayerHasTakenPassedCards) event);
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
        players.forEach(player -> takeDealtCards(player, event.playerHands()));
        roundNumber++;
    }

    private void takeDealtCards(Player player, Map<PlayerId, List<Card>> dealtCards) {
        List<Card> hand = dealtCards.get(player.id());
        player.takeDealtCards(hand);
    }

    public void applyEvent(PlayerPassedCards event) {
        Player fromPlayer = players.getPlayerById(event.fromPlayer());
        fromPlayer.passCardsTo(event.toPlayer(), event.passedCards(), table);
    }

    private void takePassedCards(PlayerId toPlayerId) {
        Player toPlayer = players.getPlayerById(toPlayerId);

        if (toPlayer.hasPassedCards() && table.hasCardsPassedTo(toPlayerId)) {
            PlayerId fromPlayer = choosePlayerToReceiveFrom(toPlayerId);
            applyNewEvent(new PlayerHasTakenPassedCards(fromPlayer, toPlayerId, table.cardsPassedTo(toPlayerId)));
        }
    }

    public void applyEvent(PlayerHasTakenPassedCards event) {
        Player toPlayer = players.getPlayerById(event.toPlayer());
        toPlayer.takePassedCardsFrom(table);
    }

    private void startPlaying() {
        Player leadingPlayer = playerWithOpeningCard();
        applyNewEvent(new StartedPlaying(leadingPlayer.id()));
    }

    private Player playerWithOpeningCard() {
        return players.where(player -> player.hand().contains(OPENING_CARD));
    }

    public void applyEvent(StartedPlaying event) {
        leadingPlayer = event.leadingPlayer();
        gamePhase = GamePhase.PLAYING;
    }

    private void applyEvent(CardPlayed event) {
        Player placedBy = players.getPlayerById(event.playedBy());

        placedBy.play(event.card(), table);
        leadingPlayer = event.nextLeadingPlayer();
    }

    private void applyEvent(TrickWon event) {
        countScores(table.trick());

        leadingPlayer = event.wonBy();
        table.clearTrick();
        tricksPlayed++;
    }

    private void countScores(Trick trick) {
        int trickScore = scoreCalculator.calculateScore(trick.cards());
        scores.compute(trick.winner(), (k, score) -> score + trickScore);
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
