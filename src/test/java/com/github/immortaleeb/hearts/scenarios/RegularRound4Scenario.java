package com.github.immortaleeb.hearts.scenarios;

import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;

import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegularRound4Scenario implements RoundScenario {
    private final List<PlayerId> players;

    public RegularRound4Scenario(List<PlayerId> players) {
        this.players = players;
    }

    @Override
    public Map<PlayerId, List<Card>> cardsDealt() {
        return fixedPlayerHands(players);
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
        Rank rank = Rank.values()[trickNumber - 1];

        return new Trick(
            List.of(
                new CardPlayed(players.get(1), Card.of(Suite.CLUBS, rank), players.get(2)),
                new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, rank), players.get(3)),
                new CardPlayed(players.get(3), Card.of(Suite.SPADES, rank), players.get(0)),
                new CardPlayed(players.get(0), Card.of(Suite.HEARTS, rank), players.get(1))
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
