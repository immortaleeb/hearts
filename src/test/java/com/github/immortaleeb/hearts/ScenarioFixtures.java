package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;
import com.github.immortaleeb.hearts.util.Events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;

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
                .addAll(cardsPassedFor(players));
    }

    public static Events playedRoundsWith(int numberOfRounds, List<PlayerId> players) {
        Events events = Events.of(new GameStarted(gameId(), players));

        for (int i = 0; i < numberOfRounds; i++) {
            events.add(new CardsDealt(fixedPlayerHands(players)));

            if ((i + 1) % 4 != 0) {
                events.addAll(cardsPassedFor(players));
                events.addAll(playRegularRoundOfCards(players));
            } else {
                events.addAll(playNonPassingRoundOfCards(players));
            }

        }

        return events
                .add(new CardsDealt(fixedPlayerHands(players)));
    }


    // helper methods
    private static Events cardsPassedFor(List<PlayerId> players) {
        return Events.of(
                new PlayerPassedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerPassedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerPassedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerPassedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),
                new PlayerHasTakenPassedCards(players.get(0), players.get(1), threeCardsOfSuite(Suite.HEARTS)),
                new PlayerHasTakenPassedCards(players.get(1), players.get(2), threeCardsOfSuite(Suite.CLUBS)),
                new PlayerHasTakenPassedCards(players.get(2), players.get(3), threeCardsOfSuite(Suite.DIAMONDS)),
                new PlayerHasTakenPassedCards(players.get(3), players.get(0), threeCardsOfSuite(Suite.SPADES)),

                new StartedPlaying(players.get(1))
        );
    }

    private static Events playRegularRoundOfCards(List<PlayerId> players) {
        return playRegular12Tricks(players)
            .addAll(playRegularTrick13(players))
            .addAll(
                new RoundEnded(new HashMap<>() {{
                    put(players.get(0), 0);
                    put(players.get(1), 25);
                    put(players.get(2), 1);
                    put(players.get(3), 0);
                }})
        );
    }

    private static List<GameEvent> playRegularTrick13(List<PlayerId> players) {
        return Arrays.asList(
            // trick 13
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.TEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.JACK), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    public static Events playRegular12Tricks(List<PlayerId> players) {
        return Events.of(
            // trick 1
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.TWO), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.ACE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.ACE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.ACE), players.get(1)),
            new TrickWon(players.get(2)),

            // trick 2
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.JACK), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.JACK), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.TWO), players.get(1)),
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.THREE), players.get(2)),
            new TrickWon(players.get(2)),

            // trick 3
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.TEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.TEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.TEN), players.get(1)),
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.QUEEN), players.get(2)),
            new TrickWon(players.get(1)),

            // trick 4
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.NINE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.KING), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.KING), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.KING), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 5
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.EIGHT), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.QUEEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.QUEEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.QUEEN), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 6
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SEVEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.NINE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.NINE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.NINE), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 7
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SIX), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.EIGHT), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.EIGHT), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.EIGHT), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 8
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FIVE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SEVEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SEVEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SEVEN), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 9
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FOUR), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SIX), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SIX), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SIX), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 10
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.KING), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FIVE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FIVE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FIVE), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 11
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.ACE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FOUR), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FOUR), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FOUR), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 12
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.JACK), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.THREE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.THREE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.THREE), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    public static Events play12TricksAnd3CardsofShootForTheMoonRound(List<PlayerId> players) {
        return Events.of(
            // trick 1
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.TWO), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.ACE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.ACE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.ACE), players.get(1)),
            new TrickWon(players.get(2)),

            // trick 2
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.JACK), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.JACK), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.JACK), players.get(1)),
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.KING), players.get(2)),
            new TrickWon(players.get(1)),

            // trick 3
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.QUEEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.TEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.TEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.TEN), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 4
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.NINE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.KING), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.KING), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.KING), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 5
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.EIGHT), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.QUEEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.QUEEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.QUEEN), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 6
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SEVEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.NINE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.NINE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.NINE), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 7
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SIX), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.EIGHT), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.EIGHT), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.EIGHT), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 8
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FIVE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SEVEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SEVEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SEVEN), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 9
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FOUR), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SIX), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SIX), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SIX), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 10
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.THREE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FIVE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FIVE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FIVE), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 11
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.ACE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FOUR), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FOUR), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FOUR), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 12
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.JACK), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.THREE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.THREE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.THREE), players.get(1)),
            new TrickWon(players.get(1)),

            // trick 13
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.TEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0))
        );
    }

    private static Events playNonPassingRoundOfCards(List<PlayerId> players) {
        Events events = new Events();

        for (Rank rank : Rank.values()) {
            events.addAll(
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, rank), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, rank), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, rank), players.get(0)),
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, rank), players.get(1)),
                new TrickWon(players.get(1))
            );
        }

        events.add(new RoundEnded(new HashMap<>() {{
            put(players.get(0), 0);
            put(players.get(1), 25);
            put(players.get(2), 1);
            put(players.get(3), 0);
        }}));

        return events;
    }

    private static GameId gameId() {
        return GameId.generate();
    }

}
