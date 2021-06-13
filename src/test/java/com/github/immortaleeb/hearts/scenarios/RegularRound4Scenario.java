package com.github.immortaleeb.hearts.scenarios;

import com.github.immortaleeb.hearts.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.List;

public class RegularRound4Scenario implements RoundScenario {
    private final List<PlayerId> players;

    public RegularRound4Scenario(List<PlayerId> players) {
        this.players = players;
    }

    @Override
    public Events trick(int trickNumber) {
        Rank rank = Rank.values()[trickNumber - 1];

        return Events.of(
            new CardPlayed(players.get(1), Card.of(Suite.CLUBS, rank), players.get(2)),
            new CardPlayed(players.get(2), Card.of(Suite.DIAMONDS, rank), players.get(3)),
            new CardPlayed(players.get(3), Card.of(Suite.SPADES, rank), players.get(0)),
            new CardPlayed(players.get(0), Card.of(Suite.HEARTS, rank), players.get(1)),
            new TrickWon(players.get(1))
        );
    }

}
