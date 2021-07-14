package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.application.PlayCard;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.fixtures.CardFixtures;
import com.github.immortaleeb.hearts.write.fixtures.GameFixtures;
import com.github.immortaleeb.hearts.write.fixtures.PlayerIdFixtures;
import com.github.immortaleeb.hearts.write.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.immortaleeb.hearts.write.fixtures.CardFixtures.twoOfClubs;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PlayerControllerTest {

    private final PlayerId player1 = PlayerIdFixtures.players().get(0);
    private final PlayerId player4 = PlayerIdFixtures.players().get(3);
    private final GameId gameId = GameId.generate();
    private final Map<PlayerId, List<Card>> dealtCards = GameFixtures.fixedPlayerHands(PlayerIdFixtures.players());

    private FakeCommandDispatcher commandDispatcher;
    private PlayerController controller;

    @BeforeEach
    void setUp() {
        commandDispatcher = new FakeCommandDispatcher();
        controller = new PlayerController(player1, commandDispatcher);
    }

    @Test
    void passes_first_3_cards_for_player_when_cards_have_been_dealt() {
        // when
        controller.process(new CardsDealt(gameId, dealtCards));

        // then
        List<Card> expectedCards = List.of(
                Card.of(Suite.HEARTS, Rank.TWO),
                Card.of(Suite.HEARTS, Rank.THREE),
                Card.of(Suite.HEARTS, Rank.FOUR)
        );
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(new PassCards(gameId, player1, expectedCards))));
    }

    @Test
    void plays_first_card_when_started_playing_and_player_is_leading_player() {
        // when
        controller.process(new StartedPlaying(gameId, player1));

        // then
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(
                new PlayCard(gameId, player1, twoOfClubs()))
        ));
    }

    @Test
    void plays_first_playable_card_when_players_turn() {
        // when
        controller.process(new CardPlayed(gameId, player4, twoOfClubs(), player1, List.of(
                Card.of(Suite.CLUBS, Rank.FIVE),
                Card.of(Suite.CLUBS, Rank.NINE)
        )));

        // then
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(
                new PlayCard(gameId, player1, Card.of(Suite.CLUBS, Rank.FIVE))
        )));
    }

    @Test
    void plays_first_card_in_hand_when_player_won_trick() {
        // given
        controller.process(new CardsDealt(gameId, dealtCards));
        controller.process(new PlayerPassedCards(player1, player4, List.of(
                Card.of(Suite.HEARTS, Rank.TWO),
                Card.of(Suite.HEARTS, Rank.THREE),
                Card.of(Suite.HEARTS, Rank.FOUR)
        )));

        // when
        controller.process(new TrickWon(gameId, player1));

        // then
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(
                new PlayCard(gameId, player1, Card.of(Suite.HEARTS, Rank.FIVE))
        )));
    }

    @Test
    void plays_next_card_in_hand_after_playing_previous_card() {
        // given
        controller.process(new CardsDealt(gameId, dealtCards));
        controller.process(new PlayerPassedCards(player1, player4, List.of(
                Card.of(Suite.HEARTS, Rank.TWO),
                Card.of(Suite.HEARTS, Rank.THREE),
                Card.of(Suite.HEARTS, Rank.FOUR)
        )));
        controller.process(new CardPlayed(gameId, player1, Card.of(Suite.HEARTS, Rank.FIVE), null, emptyList()));

        // when
        controller.process(new TrickWon(gameId, player1));

        // then
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(
                new PlayCard(gameId, player1, Card.of(Suite.HEARTS, Rank.SIX))
        )));
    }
}