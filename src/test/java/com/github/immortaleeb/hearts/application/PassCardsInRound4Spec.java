package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.CardPlayed;
import com.github.immortaleeb.hearts.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerAlreadyPassedCards;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;
import com.github.immortaleeb.hearts.util.Events;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.CardFixtures.twoOfClubs;
import static com.github.immortaleeb.hearts.ScenarioFixtures.playedRoundsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PassCardsInRound4Spec extends GameSpec {

    private List<PlayerId> players;

    @Override
    protected Events given() {
        players = PlayerIdFixtures.players();
        return playedRoundsWith(3, players);
    }

    @Test
    void cards_cannot_be_passed_in_round_4() {
        assertThrows(PlayerAlreadyPassedCards.class, () -> passCards(player1(), threeCardsOfSuite(Suite.HEARTS)));
    }

    @Test
    void cards_can_be_played_in_round_4() {
        playCard(player2(), twoOfClubs());

        assertEvent(CardPlayed.class, event -> {
            assertThat(event.playedBy(), is(equalTo(player2())));
            assertThat(event.card(), is(equalTo(twoOfClubs())));
        });
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
