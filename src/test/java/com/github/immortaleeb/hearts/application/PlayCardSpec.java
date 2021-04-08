package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.CardPlayed;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.domain.RoundEnded;
import com.github.immortaleeb.hearts.domain.TrickWon;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardNotInHand;
import com.github.immortaleeb.hearts.shared.InvalidCardPlayed;
import com.github.immortaleeb.hearts.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.CardFixtures.twoOfClubs;
import static com.github.immortaleeb.hearts.ScenarioFixtures.round1StartedWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayCardSpec extends GameSpec {

    private List<PlayerId> players;

    @Override
    protected List<GameEvent> given() {
        players = PlayerIdFixtures.players();
        return round1StartedWith(players);
    }

    @Test
    void player_cannot_play_card_when_its_not_their_turn() {
        assertThrows(NotPlayersTurn.class, () -> playCard(player1(), Card.of(Suite.HEARTS, Rank.TEN)));
    }

    @Test
    void player_can_only_play_cards_from_their_hand() {
        assertThrows(CardNotInHand.class, () -> playCard(player2(), Card.of(Suite.SPADES, Rank.QUEEN)));
    }

    @Test
    void player_cannot_play_card_twice_in_a_row() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertThrows(NotPlayersTurn.class, () -> playCard(player1(), Card.of(Suite.CLUBS, Rank.TEN)));
    }

    @Test
    void player_cannot_play_when_its_not_their_turn_after_card_is_played() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertThrows(NotPlayersTurn.class, () -> playCard(player4(), Card.of(Suite.DIAMONDS, Rank.TWO)));
    }

    @Test
    void playing_a_card_moves_leading_player_to_player_3_after_player_2() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));

        assertEvent(CardPlayed.class, event -> {
            assertThat(event.nextLeadingPlayer(), is(equalTo(player3())));
        });
    }

    @Test
    void playing_a_card_moves_leading_player_to_player_4_after_player_3() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));

        List<CardPlayed> events = getEvents(CardPlayed.class);
        assertThat(events.get(1).nextLeadingPlayer(), is(equalTo(player4())));
    }

    @Test
    void leading_player_must_open_round_with_two_of_clubs() {
        InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE)));

        assertThat(exception.getMessage(), is(equalTo("You must open with the two of clubs")));
    }

    @Test
    void player2_plays_card() {
        playCard(player2(), twoOfClubs());

        assertEvent(CardPlayed.class, event -> {
            assertThat(event.playedBy(), is(equalTo(player2())));
            assertThat(event.card(), is(equalTo(twoOfClubs())));
        });
    }

    @Test
    void player_cannot_play_any_card_that_they_passed_to_another_player() {
        playCard(player2(), twoOfClubs());

        List<Card> passedCards = threeCardsOfSuite(Suite.DIAMONDS);

        assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(0)));
        assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(1)));
        assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(2)));
    }

    @Test
    void player_can_play_a_card_they_received_from_another_player() {
        // before
        playCard(player2(), twoOfClubs());

        // given
        List<Card> receivedCards = threeCardsOfSuite(Suite.CLUBS);
        Card receivedCard = receivedCards.get(0);

        // when
        playCard(player3(), receivedCard);

        // then
        List<CardPlayed> events = getEvents(CardPlayed.class);
        assertThat(events.get(1).card(), is(equalTo(receivedCard)));
    }

    @Test
    void trick_is_not_won_until_all_players_played_cards() {
        assertNoEvent(TrickWon.class);

        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        assertNoEvent(TrickWon.class);

        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        assertNoEvent(TrickWon.class);

        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        assertNoEvent(TrickWon.class);

        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));
        assertEvent(TrickWon.class);
    }

    @Test
    void player_must_follow_suite() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));

        InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(player3(), Card.of(Suite.DIAMONDS, Rank.TWO)));

        assertThat(exception.getMessage(), is(equalTo("Player must follow suite when possible")));
    }

    @Test
    void player_with_highest_card_following_trick_suite_wins_trick() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));

        assertEvent(TrickWon.class, event -> {
            assertThat(event.wonBy(), is(equalTo(player3())));
        });
    }

    @Test
    void player_who_won_the_trick_is_the_next_leading_player() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));

        assertThrows(NotPlayersTurn.class, () -> playCard(player1(), Card.of(Suite.HEARTS, Rank.THREE)));
        assertThrows(NotPlayersTurn.class, () -> playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE)));
        assertThrows(NotPlayersTurn.class, () -> playCard(player4(), Card.of(Suite.SPADES, Rank.THREE)));

        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.THREE));

        assertThat(getEvents(CardPlayed.class), hasSize(5));
    }

    @Test
    void player_cannot_play_already_played_card() {
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));

        assertThrows(CardNotInHand.class, () -> playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN)));
    }

    @Test
    void round_ends_when_all_players_have_played_13_cards() {
        playFullRound(trick -> {
            if (trick < 13) {
                assertNoEvent(RoundEnded.class);
            }
        });

        assertEvent(RoundEnded.class);
    }

    @Test
    void round_ends_with_correct_scores() {
        playFullRound(trick -> {});

        assertEvent(RoundEnded.class, event -> {
            Map<PlayerId, Integer> scores = event.scores();
            assertThat(scores.get(player1()), is(equalTo(0)));
            assertThat(scores.get(player2()), is(equalTo(25)));
            assertThat(scores.get(player3()), is(equalTo(1)));
            assertThat(scores.get(player4()), is(equalTo(0)));
        });
    }

    @Test
    void player_who_shot_for_the_moon_has_score_minus_26() {
        playShootForTheMoonRound(trick -> {});

        assertEvent(RoundEnded.class, event -> {
            Map<PlayerId, Integer> scores = event.scores();
            assertThat(scores.get(player1()), is(equalTo(0)));
            assertThat(scores.get(player2()), is(equalTo(-26)));
            assertThat(scores.get(player3()), is(equalTo(0)));
            assertThat(scores.get(player4()), is(equalTo(0)));
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

    private void playFullRound(Consumer<Integer> afterTrick) {
        // trick 1
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.ACE));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.ACE));
        playCard(player1(), Card.of(Suite.SPADES, Rank.ACE));
        afterTrick.accept(1);

        // trick 2
        playCard(player3(), Card.of(Suite.CLUBS, Rank.JACK));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.JACK));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));
        playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE));
        afterTrick.accept(2);

        // trick 3
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.TEN));
        playCard(player1(), Card.of(Suite.SPADES, Rank.TEN));
        playCard(player2(), Card.of(Suite.CLUBS, Rank.QUEEN));
        afterTrick.accept( 3);

        // trick 4
        playCard(player2(), Card.of(Suite.CLUBS, Rank.NINE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.KING));
        playCard(player4(), Card.of(Suite.SPADES, Rank.KING));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.KING));
        afterTrick.accept( 4);

        // trick 5
        playCard(player2(), Card.of(Suite.CLUBS, Rank.EIGHT));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.QUEEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.QUEEN));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.QUEEN));
        afterTrick.accept( 5);

        // trick 6
        playCard(player2(), Card.of(Suite.CLUBS, Rank.SEVEN));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.NINE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.NINE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.NINE));
        afterTrick.accept( 6);

        // trick 7
        playCard(player2(), Card.of(Suite.CLUBS, Rank.SIX));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.EIGHT));
        playCard(player4(), Card.of(Suite.SPADES, Rank.EIGHT));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.EIGHT));
        afterTrick.accept( 7);

        // trick 8
        playCard(player2(), Card.of(Suite.CLUBS, Rank.FIVE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.SEVEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.SEVEN));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.SEVEN));
        afterTrick.accept( 8);

        // trick 9
        playCard(player2(), Card.of(Suite.CLUBS, Rank.FOUR));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.SIX));
        playCard(player4(), Card.of(Suite.SPADES, Rank.SIX));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.SIX));
        afterTrick.accept( 9);

        // trick 10
        playCard(player2(), Card.of(Suite.CLUBS, Rank.KING));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.FIVE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.FIVE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.FIVE));
        afterTrick.accept(10);

        // trick 11
        playCard(player2(), Card.of(Suite.HEARTS, Rank.ACE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.FOUR));
        playCard(player4(), Card.of(Suite.SPADES, Rank.FOUR));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.FOUR));
        afterTrick.accept(11);

        // trick 12
        playCard(player2(), Card.of(Suite.HEARTS, Rank.JACK));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.THREE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.THREE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.THREE));
        afterTrick.accept(12);

        // trick 13
        playCard(player2(), Card.of(Suite.HEARTS, Rank.TEN));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.TWO));
        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(player1(), Card.of(Suite.SPADES, Rank.JACK));
        afterTrick.accept(13);
    }

    private void playShootForTheMoonRound(Consumer<Integer> afterTrick) {
        // trick 1
        playCard(player2(), Card.of(Suite.CLUBS, Rank.TWO));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.ACE));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.ACE));
        playCard(player1(), Card.of(Suite.SPADES, Rank.ACE));
        afterTrick.accept(1);

        // trick 2
        playCard(player3(), Card.of(Suite.CLUBS, Rank.JACK));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.JACK));
        playCard(player1(), Card.of(Suite.SPADES, Rank.JACK));
        playCard(player2(), Card.of(Suite.CLUBS, Rank.KING));
        afterTrick.accept(2);

        // trick 3
        playCard(player2(), Card.of(Suite.CLUBS, Rank.QUEEN));
        playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));
        playCard(player4(), Card.of(Suite.DIAMONDS, Rank.TEN));
        playCard(player1(), Card.of(Suite.SPADES, Rank.TEN));
        afterTrick.accept( 3);

        // trick 4
        playCard(player2(), Card.of(Suite.CLUBS, Rank.NINE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.KING));
        playCard(player4(), Card.of(Suite.SPADES, Rank.KING));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.KING));
        afterTrick.accept( 4);

        // trick 5
        playCard(player2(), Card.of(Suite.CLUBS, Rank.EIGHT));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.QUEEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.QUEEN));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.QUEEN));
        afterTrick.accept( 5);

        // trick 6
        playCard(player2(), Card.of(Suite.CLUBS, Rank.SEVEN));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.NINE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.NINE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.NINE));
        afterTrick.accept( 6);

        // trick 7
        playCard(player2(), Card.of(Suite.CLUBS, Rank.SIX));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.EIGHT));
        playCard(player4(), Card.of(Suite.SPADES, Rank.EIGHT));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.EIGHT));
        afterTrick.accept( 7);

        // trick 8
        playCard(player2(), Card.of(Suite.CLUBS, Rank.FIVE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.SEVEN));
        playCard(player4(), Card.of(Suite.SPADES, Rank.SEVEN));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.SEVEN));
        afterTrick.accept( 8);

        // trick 9
        playCard(player2(), Card.of(Suite.CLUBS, Rank.FOUR));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.SIX));
        playCard(player4(), Card.of(Suite.SPADES, Rank.SIX));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.SIX));
        afterTrick.accept( 9);

        // trick 10
        playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.FIVE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.FIVE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.FIVE));
        afterTrick.accept(10);

        // trick 11
        playCard(player2(), Card.of(Suite.HEARTS, Rank.ACE));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.FOUR));
        playCard(player4(), Card.of(Suite.SPADES, Rank.FOUR));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.FOUR));
        afterTrick.accept(11);

        // trick 12
        playCard(player2(), Card.of(Suite.HEARTS, Rank.JACK));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.THREE));
        playCard(player4(), Card.of(Suite.SPADES, Rank.THREE));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.THREE));
        afterTrick.accept(12);

        // trick 13
        playCard(player2(), Card.of(Suite.HEARTS, Rank.TEN));
        playCard(player3(), Card.of(Suite.DIAMONDS, Rank.TWO));
        playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));
        afterTrick.accept(13);
    }

}
