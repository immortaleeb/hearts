package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.Rank;
import com.github.immortaleeb.hears.common.shared.Suite;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.application.fixtures.GameFixtures;
import com.github.immortaleeb.hearts.write.application.fixtures.PlayerIdFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.immortaleeb.hearts.write.application.fixtures.CardFixtures.twoOfClubs;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
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
        HeartsWriteApi heartsWriteApi = new InMemoryHeartsWriteApi(commandDispatcher);
        controller = new PlayerController(player1, heartsWriteApi, new SimplePlayerInputHandler());
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

    @Test
    void plays_received_card_when_no_other_cards_are_left() {
        // given
        List<Card> fixedHand = List.of(
                Card.of(Suite.SPADES, Rank.TWO),
                Card.of(Suite.SPADES, Rank.THREE),
                Card.of(Suite.SPADES, Rank.FOUR)
        );

        controller.process(new CardsDealt(gameId, fixedHands(fixedHand)));
        controller.process(new PlayerPassedCards(player1, player4, fixedHand));
        controller.process(new PlayerHasTakenPassedCards(player4, player1, List.of(
                Card.of(Suite.DIAMONDS, Rank.NINE),
                Card.of(Suite.DIAMONDS, Rank.QUEEN),
                Card.of(Suite.DIAMONDS, Rank.KING)
        )));

        // when
        controller.process(new TrickWon(gameId, player1));

        // then
        assertThat(commandDispatcher.lastDispatchedCommand(), is(equalTo(
                new PlayCard(gameId, player1, Card.of(Suite.DIAMONDS, Rank.NINE))
        )));
    }

    @Test
    void does_not_pass_cards_on_every_4th_round() {
        // given
        controller.process(new CardsDealt(gameId, dealtCards));
        controller.process(new CardsDealt(gameId, dealtCards));
        controller.process(new CardsDealt(gameId, dealtCards));

        // when
        controller.process(new CardsDealt(gameId, dealtCards));

        // then
        assertThat(commandDispatcher.dispatchedCommands(), hasSize(3));

    }

    // helper methods

    private Map<PlayerId, List<Card>> fixedHands(List<Card> hand) {
        return PlayerIdFixtures.players().stream()
                .collect(Collectors.toMap(Function.identity(), player -> unmodifiableList(hand)));
    }

}