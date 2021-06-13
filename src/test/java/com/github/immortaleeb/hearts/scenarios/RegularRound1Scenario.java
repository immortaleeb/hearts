package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.List;

public class RegularRound1Scenario extends FixedRoundScenario {

    private final List<PlayerId> players;

    public RegularRound1Scenario(List<PlayerId> players) {
        this.players = players;
    }

    @Override
    protected Events trick1() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.TWO), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.ACE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.ACE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.ACE), players.get(1)),
            new TrickWon(players.get(2))
        );
    }

    @Override
    protected Events trick2() {
        return Events.of(
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.JACK), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.JACK), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.TWO), players.get(1)),
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.THREE), players.get(2)),
            new TrickWon(players.get(2))
        );
    }

    @Override
    protected Events trick3() {
        return Events.of(
            new CardPlayed(players.get(2), Card.of(Suite.CLUBS, Rank.TEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.DIAMONDS, Rank.TEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.TEN), players.get(1)),
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.QUEEN), players.get(2)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick4() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.NINE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.KING), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.KING), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.KING), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick5() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.EIGHT), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.QUEEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.QUEEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.QUEEN), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick6() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SEVEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.NINE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.NINE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.NINE), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick7() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.SIX), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.EIGHT), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.EIGHT), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.EIGHT), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick8() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FIVE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SEVEN), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SEVEN), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SEVEN), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick9() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.FOUR), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.SIX), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.SIX), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.SIX), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick10() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, Rank.KING), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FIVE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FIVE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FIVE), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick11() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.ACE), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.FOUR), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.FOUR), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.FOUR), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick12() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.JACK), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.THREE), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.THREE), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, Rank.THREE), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

    @Override
    protected Events trick13() {
        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.HEARTS, Rank.TEN), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, Rank.TWO), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, Rank.TWO), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.SPADES, Rank.JACK), players.get(1)),
            new TrickWon(players.get(1))
        );
    }
}
