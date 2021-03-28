package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.CardPlayed;
import com.github.immortaleeb.hearts.domain.CardsDealt;
import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameStarted;
import com.github.immortaleeb.hearts.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.domain.PlayerReceivedCards;
import com.github.immortaleeb.hearts.domain.RoundStarted;
import com.github.immortaleeb.hearts.domain.TrickWon;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardNotInHand;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.InvalidCardPlayed;
import com.github.immortaleeb.hearts.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.CardFixtures.twoOfClubs;
import static com.github.immortaleeb.hearts.GameFixtures.fixedPlayerHands;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayCardSpec extends GameSpec {

    private List<PlayerId> players;
    private GameId gameId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        players = PlayerIdFixtures.players();

        gameId = GameId.generate();

        Game game = new Game(gameId);

        game.applyNewEvent(new GameStarted(players));
        game.applyNewEvent(new CardsDealt(fixedPlayerHands(players)));

        game.applyNewEvent(new PlayerPassedCards(player1(), player2(), threeCardsOfSuite(Suite.HEARTS)));
        game.applyNewEvent(new PlayerPassedCards(player2(), player3(), threeCardsOfSuite(Suite.CLUBS)));
        game.applyNewEvent(new PlayerPassedCards(player3(), player4(), threeCardsOfSuite(Suite.DIAMONDS)));
        game.applyNewEvent(new PlayerPassedCards(player4(), player1(), threeCardsOfSuite(Suite.SPADES)));

        game.applyNewEvent(new PlayerReceivedCards(player1(), player2(), threeCardsOfSuite(Suite.HEARTS)));
        game.applyNewEvent(new PlayerReceivedCards(player2(), player3(), threeCardsOfSuite(Suite.CLUBS)));
        game.applyNewEvent(new PlayerReceivedCards(player3(), player4(), threeCardsOfSuite(Suite.DIAMONDS)));
        game.applyNewEvent(new PlayerReceivedCards(player4(), player1(), threeCardsOfSuite(Suite.SPADES)));

        game.applyNewEvent(new RoundStarted(player2()));

        gameRepository.save(game);
    }

    @Test
    void player_cannot_play_card_when_its_not_their_turn() {
        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player1(), Card.of(Suite.HEARTS, Rank.TEN)));
    }

    @Test
    void player_can_only_play_cards_from_their_hand() {
        assertThrows(CardNotInHand.class, () -> playCard(gameId, player2(), Card.of(Suite.SPADES, Rank.QUEEN)));
    }

    @Test
    void player_cannot_play_card_twice_in_a_row() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player1(), Card.of(Suite.CLUBS, Rank.TEN)));
    }

    @Test
    void player_cannot_play_when_its_not_their_turn_after_card_is_played() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player4(), Card.of(Suite.DIAMONDS, Rank.TWO)));
    }

    @Test
    void playing_a_card_moves_leading_player_to_player_3_after_player_2() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));

        assertEvent(gameId, CardPlayed.class, event -> {
            assertThat(event.nextLeadingPlayer(), is(equalTo(player3())));
        });
    }

    @Test
    void playing_a_card_moves_leading_player_to_player_4_after_player_3() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN));

        List<CardPlayed> events = getEvents(gameId, CardPlayed.class);
        assertThat(events.get(1).nextLeadingPlayer(), is(equalTo(player4())));
    }

    @Test
    void leading_player_must_open_round_with_two_of_clubs() {
        InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.THREE)));

        assertThat(exception.getMessage(), is(equalTo("You must open with the two of clubs")));
    }

    @Test
    void player2_plays_card() {
        playCard(gameId, player2(), twoOfClubs());

        assertEvent(gameId, CardPlayed.class, event -> {
            assertThat(event.playedBy(), is(equalTo(player2())));
            assertThat(event.card(), is(equalTo(twoOfClubs())));
        });
    }

    @Test
    void player_cannot_play_any_card_that_they_passed_to_another_player() {
        playCard(gameId, player2(), twoOfClubs());

        List<Card> passedCards = threeCardsOfSuite(Suite.DIAMONDS);

        assertThrows(CardNotInHand.class, () -> playCard(gameId, player3(), passedCards.get(0)));
        assertThrows(CardNotInHand.class, () -> playCard(gameId, player3(), passedCards.get(1)));
        assertThrows(CardNotInHand.class, () -> playCard(gameId, player3(), passedCards.get(2)));
    }

    @Test
    void player_can_play_a_card_they_received_from_another_player() {
        // before
        playCard(gameId, player2(), twoOfClubs());

        // given
        List<Card> receivedCards = threeCardsOfSuite(Suite.CLUBS);
        Card receivedCard = receivedCards.get(0);

        // when
        playCard(gameId, player3(), receivedCard);

        // then
        List<CardPlayed> events = getEvents(gameId, CardPlayed.class);
        assertThat(events.get(1).card(), is(equalTo(receivedCard)));
    }

    @Test
    void trick_is_not_won_until_all_players_played_cards() {
        assertNoEvent(gameId, TrickWon.class);

        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertNoEvent(gameId, TrickWon.class);

        playCard(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN));
        assertNoEvent(gameId, TrickWon.class);

        playCard(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO));
        assertNoEvent(gameId, TrickWon.class);

        playCard(gameId, player1(), Card.of(Suite.HEARTS, Rank.TWO));
        assertEvent(gameId, TrickWon.class);
    }

    @Test
    void player_must_follow_suite() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));

        InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(gameId, player3(), Card.of(Suite.DIAMONDS, Rank.TWO)));

        assertThat(exception.getMessage(), is(equalTo("Player must follow suite when possible")));
    }

    @Test
    void player_with_highest_card_following_trick_suite_wins_trick() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(gameId, player1(), Card.of(Suite.HEARTS, Rank.TWO));

        assertEvent(gameId, TrickWon.class, event -> {
            assertThat(event.wonBy(), is(equalTo(player3())));
        });
    }

    @Test
    void player_who_won_the_trick_is_the_next_leading_player() {
        playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(gameId, player1(), Card.of(Suite.HEARTS, Rank.TWO));

        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player1(), Card.of(Suite.HEARTS, Rank.THREE)));
        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player2(), Card.of(Suite.CLUBS, Rank.THREE)));
        assertThrows(NotPlayersTurn.class, () -> playCard(gameId, player4(), Card.of(Suite.SPADES, Rank.THREE)));

        playCard(gameId, player3(), Card.of(Suite.DIAMONDS, Rank.THREE));

        assertThat(getEvents(gameId, CardPlayed.class), hasSize(5));
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
