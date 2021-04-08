package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Suite;
import com.github.immortaleeb.hearts.util.Events;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.ScenarioFixtures.playedRoundsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PassCardsInRound3Spec extends GameSpec {

    private List<PlayerId> players;

    @Override
    protected Events given() {
        players = PlayerIdFixtures.players();
        return playedRoundsWith(2, players);
    }

    @Test
    void cards_are_passed_to_front_in_round_3() {
        passCards(player1(), threeCardsOfSuite(Suite.HEARTS));
        passCards(player4(), threeCardsOfSuite(Suite.SPADES));
        passCards(player3(), threeCardsOfSuite(Suite.DIAMONDS));
        passCards(player2(), threeCardsOfSuite(Suite.CLUBS));

        List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

        assertThat(passEvents.get(0).toPlayer(), is(equalTo(player3())));
        assertThat(passEvents.get(1).toPlayer(), is(equalTo(player2())));
        assertThat(passEvents.get(2).toPlayer(), is(equalTo(player1())));
        assertThat(passEvents.get(3).toPlayer(), is(equalTo(player4())));
    }

    // helper methods

    private PlayerId player1() {
        return players.get(0);
    }

    private PlayerId player2() {
        return players.get(1);
    }

    private PlayerId player3() {
        return players.get(2);
    }

    private PlayerId player4() {
        return players.get(3);
    }

}
