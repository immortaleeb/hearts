package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private static final int CARDS_PER_HAND = 13;
    private static final int CARDS_TO_PASS = 3;
    private static final Card OPENING_CARD = Card.of(Suite.CLUBS, Rank.TWO);
    private static final int TRICKS_PER_ROUND = 13;
    private static final int SHOOT_FOR_THE_MOON_SCORE = 26;
    private static final int SCORE_FOR_GAME_END = 100;

    private final GameId id;

    private Table table;
    private final Dealer dealer = new Dealer(CARDS_PER_HAND);
    private final ScoreCalculator scoreCalculator = new ScoreCalculator(SHOOT_FOR_THE_MOON_SCORE);

    private final CardPassValidator cardPassValidator = new CardPassValidator(CARDS_TO_PASS);
    private final CardPlayValidator cardPlayValidator = new CardPlayValidator(OPENING_CARD);
    private final CardPlayFilter cardPlayFilter = new CardPlayFilter(cardPlayValidator);

    private Players players;
    private PlayerId leadingPlayer;
    private int tricksPlayed = 0;
    private int roundNumber = 0;
    private GamePhase gamePhase = GamePhase.DEALING;
    private Scoreboard scoreboard;

    private final List<GameEvent> raisedEvents = new ArrayList<>();

    public Game(GameId id) {
        this.id = id;
    }

    public GameId id() {
        return id;
    }

    private void dealCards() {
        Map<PlayerId, List<Card>> playerHands = dealer.deal(players.ids());
        applyNewEvent(new CardsDealt(id, playerHands));

        if (!shouldPassCardsThisRound()) {
            startPlaying();
        }
    }

    public void passCards(PlayerId fromPlayerId, List<Card> cards) {
        if (!shouldPassCardsThisRound()) {
            throw new NoCardsNeedToBePassed();
        }

        Player fromPlayer = players.getPlayerById(fromPlayerId);
        cardPassValidator.verifyPassIsValid(fromPlayer, cards);

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

        ValidationResult validationResult = validateCardPlay(playerId, card);
        if (validationResult.hasError()) {
            throw new InvalidCardPlayed(validationResult.error());
        }

        Optional<PlayerId> nextLeadingPlayer = chooseNextLeadingPlayer(playerId);
        Trick updatedTrick = table.trick().play(card, playerId);

        List<Card> validCardsToPlay = filterValidCardsToPlay(nextLeadingPlayer.stream(), updatedTrick);

        applyNewEvent(new CardPlayed(id, playerId, card, nextLeadingPlayer.orElse(null), validCardsToPlay));

        if (trickFinished(table.numberOfPlayedCards())) {
            applyNewEvent(new TrickWon(table.trick().winner()));
        }

        if (tricksPlayed == TRICKS_PER_ROUND) {
            applyNewEvent(new RoundEnded(scoreCalculator.countRoundScores(table.wonTricks())));

            if (scoreboard.largestScore().score() >= SCORE_FOR_GAME_END) {
                applyNewEvent(new GameEnded(scoreboard.toMap()));
            } else {
                dealCards();
            }
        }
    }

    private List<Card> filterValidCardsToPlay(Stream<PlayerId> nextLeadingPlayer, Trick updatedTrick) {
        return nextLeadingPlayer
                .map(players::getPlayerById)
                .map(Player::hand)
                .map(playerHand -> cardPlayFilter.filterValidCardsToPlayFromHand(updatedTrick, playerHand))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean trickFinished(int numberOfPlayedCards) {
        return numberOfPlayedCards == players.size();
    }

    private ValidationResult validateCardPlay(PlayerId playerId, Card card) {
        Player player = players.getPlayerById(playerId);

        return cardPlayValidator.validateCardPlay(table.trick(), player.hand(), card);
    }

    private Optional<PlayerId> chooseNextLeadingPlayer(PlayerId player) {
        if (trickFinished(table.numberOfPlayedCards() + 1)) {
            return Optional.empty();
        }

        return Optional.of(players.choosePlayerWithOffset(player, 1));
    }

    private PlayerId choosePlayerToPassTo(PlayerId fromPlayer) {
        return players.choosePlayerWithOffset(fromPlayer, choosePassingDirection().get().toOffset());
    }

    private PlayerId choosePlayerToReceiveFrom(PlayerId toPlayer) {
        return players.choosePlayerWithOffset(toPlayer, choosePassingDirection().get().fromOffset());
    }

    private boolean shouldPassCardsThisRound() {
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
        } else if (event instanceof GameEnded) {
            applyEvent((GameEnded) event);
        } else {
            throw new RuntimeException("Unknown event type " + event.getClass());
        }
    }

    public void applyEvent(GameStarted event) {
        players = Players.of(event.players());
        table = Table.with(event.players());
        scoreboard = Scoreboard.forPlayers(event.players());
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
        applyNewEvent(new StartedPlaying(id, leadingPlayer.id()));
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
        leadingPlayer = event.nextLeadingPlayer().orElse(null);
    }

    private void applyEvent(TrickWon event) {
        leadingPlayer = event.wonBy();
        table.clearTrick();
        tricksPlayed++;
    }

    private void applyEvent(RoundEnded event) {
        table.clearWonTricks();
        tricksPlayed = 0;
        scoreboard = scoreboard.add(event.scores());
    }

    private void applyEvent(GameEnded event) {

    }

    public static Game startWith(List<PlayerId> players) {
        Game game = new Game(GameId.generate());
        game.applyNewEvent(new GameStarted(game.id, players));

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
