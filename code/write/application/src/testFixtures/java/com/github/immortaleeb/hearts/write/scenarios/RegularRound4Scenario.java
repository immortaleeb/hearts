package com.github.immortaleeb.hearts.write.scenarios;

import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.*;
import com.github.immortaleeb.hearts.write.fixtures.CardFixtures;

import java.util.*;

import static java.util.Collections.emptyList;

public class RegularRound4Scenario implements RoundScenario {

    private final GameId gameId;
    private final List<PlayerId> players;

    public RegularRound4Scenario(GameId gameId, List<PlayerId> players) {
        this.gameId = gameId;
        this.players = players;
    }

    @Override
    public Map<PlayerId, List<Card>> cardsDealt() {
        Map<PlayerId, List<Card>> fixedHands = new HashMap<>();

        fixedHands.put(players.get(0), generateHandFor(0));
        fixedHands.put(players.get(1), generateHandFor(1));
        fixedHands.put(players.get(2), generateHandFor(2));
        fixedHands.put(players.get(3), generateHandFor(3));

        return fixedHands;
    }

    private List<Card> generateHandFor(int playerIndex) {
        Suite suite = Suite.values()[playerIndex % 4];
        Suite suiteOfAce = switch (playerIndex % 4) {
            case 1 -> Suite.DIAMONDS;
            case 2 -> Suite.CLUBS;
            default -> suite;
        };

        List<Card> cards = new ArrayList<>(CardFixtures.allCardsOfSuiteExcept(suite, card -> card.rank() == Rank.ACE));

        cards.add(Card.of(suiteOfAce, Rank.ACE));

        return cards;
    }

    @Override
    public PassedCards cardsPassed() {
        return PassedCards.none();
    }

    @Override
    public PlayerId leadingPlayer() {
        return players.get(1);
    }

    @Override
    public Trick trick(int trickNumber) {
        return switch (trickNumber) {
            case 1 -> trick1();
            case 2 -> trick2();
            default -> otherTrick(trickNumber);
        };
    }

    private Trick otherTrick(int trickNumber) {
        Rank rank = Rank.values()[trickNumber - 2];

        return new Trick(
            List.of(
                new CardPlayed(gameId, players.get(1), Card.of(Suite.CLUBS, rank), players.get(2), emptyList()),
                new CardPlayed(gameId, players.get(2), Card.of(Suite.DIAMONDS, rank), players.get(3), emptyList()),
                new CardPlayed(gameId, players.get(3), Card.of(Suite.SPADES, rank), players.get(0), emptyList()),
                new CardPlayed(gameId, players.get(0), Card.of(Suite.HEARTS, rank), players.get(1), emptyList())
            ),
            new TrickWon(players.get(1))
        );
    }

    private Trick trick1() {
        return new Trick(
            List.of(
                new CardPlayed(gameId, players.get(1), Card.of(Suite.CLUBS, Rank.TWO), players.get(2), emptyList()),
                new CardPlayed(gameId, players.get(2), Card.of(Suite.CLUBS, Rank.ACE), players.get(3), emptyList()),
                new CardPlayed(gameId, players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0), emptyList()),
                new CardPlayed(gameId, players.get(0), Card.of(Suite.HEARTS, Rank.TWO), players.get(1), emptyList())
            ),
            new TrickWon(players.get(2))
        );
    }

    private Trick trick2() {
        return new Trick(
            List.of(
                new CardPlayed(gameId, players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3), emptyList()),
                new CardPlayed(gameId, players.get(3), Card.of(Suite.SPADES, Rank.ACE), players.get(0), emptyList()),
                new CardPlayed(gameId, players.get(0), Card.of(Suite.HEARTS, Rank.ACE), players.get(1), emptyList()),
                new CardPlayed(gameId, players.get(1), Card.of(Suite.DIAMONDS, Rank.ACE), players.get(2), emptyList())
            ),
            new TrickWon(players.get(1))
        );
    }

    @Override
    public Map<PlayerId, Integer> roundScore() {
        return new HashMap<>() {{
            put(players.get(0), 0);
            put(players.get(1), 25);
            put(players.get(2), 1);
            put(players.get(3), 0);
        }};
    }

}
