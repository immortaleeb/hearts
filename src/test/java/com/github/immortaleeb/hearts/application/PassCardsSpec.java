package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.domain.PlayerReceivedCards;
import com.github.immortaleeb.hearts.domain.StartedPlaying;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.shared.IncorrectNumberOfCardsPassed;
import com.github.immortaleeb.hearts.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.shared.PlayerAlreadyPassedCards;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;
import com.github.immortaleeb.hearts.util.Events;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.immortaleeb.hearts.CardFixtures.threeCardsOfSuite;
import static com.github.immortaleeb.hearts.CardFixtures.twoOfClubs;
import static com.github.immortaleeb.hearts.ScenarioFixtures.gameStartedWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PassCardsSpec extends GameSpec {

    private List<PlayerId> players;

    @Override
    protected Events given() {
        players = PlayerIdFixtures.players();
        return gameStartedWith(players);
    }

    @Test
    void player_cannot_pass_less_than_three_cards() {
        List<Card> cards = Arrays.asList(
            Card.of(Suite.SPADES, Rank.TEN),
            Card.of(Suite.DIAMONDS, Rank.TWO)
        );

        assertThrows(IncorrectNumberOfCardsPassed.class, () -> passCards(player1(), cards));
    }

    @Test
    void player_cannot_pass_more_than_three_cards() {
        List<Card> cards = Arrays.asList(
                Card.of(Suite.SPADES, Rank.TEN),
                Card.of(Suite.DIAMONDS, Rank.TWO),
                Card.of(Suite.CLUBS, Rank.ACE),
                Card.of(Suite.DIAMONDS, Rank.JACK)
        );

        assertThrows(IncorrectNumberOfCardsPassed.class, () -> passCards(player1(), cards));
    }

    @Test
    void player_can_pass_three_cards() {
        List<Card> cards = threeCardsOfSuite(Suite.HEARTS);

        passCards(player1(), cards);

        assertEvent(PlayerPassedCards.class, event -> {
            assertThat(event.fromPlayer(), is(equalTo(player1())));
            assertThat(event.passedCards(), is(equalTo(cards)));
        });
    }

    @Test
    void player_can_only_pass_cards_in_their_hand() {
        List<Card> cards = Arrays.asList(
                Card.of(Suite.HEARTS, Rank.TEN),
                Card.of(Suite.HEARTS, Rank.JACK),
                Card.of(Suite.CLUBS, Rank.ACE)
        );

        assertThrows(CardsNotInHand.class, () -> passCards(player1(), cards));
    }

    @Test
    void player_cannot_pass_cards_twice() {
        List<Card> cards = threeCardsOfSuite(Suite.HEARTS);
        List<Card> otherCards = Arrays.asList(
                Card.of(Suite.HEARTS, Rank.FIVE),
                Card.of(Suite.HEARTS, Rank.SIX),
                Card.of(Suite.HEARTS, Rank.SEVEN)
        );

        passCards(player1(), cards);
        assertThrows(PlayerAlreadyPassedCards.class, () -> passCards(player1(), otherCards));
    }

    @Test
    void cards_are_passed_to_left_in_round_1() {
        passCards(player1(), threeCardsOfSuite(Suite.HEARTS));
        passCards(player4(), threeCardsOfSuite(Suite.SPADES));
        passCards(player3(), threeCardsOfSuite(Suite.DIAMONDS));
        passCards(player2(), threeCardsOfSuite(Suite.CLUBS));

        List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

        assertThat(passEvents.get(0).toPlayer(), is(equalTo(player2())));
        assertThat(passEvents.get(1).toPlayer(), is(equalTo(player1())));
        assertThat(passEvents.get(2).toPlayer(), is(equalTo(player4())));
        assertThat(passEvents.get(3).toPlayer(), is(equalTo(player3())));
    }

    @Test
    void player_receives_cards_after_passing_their_cards() {
        assertNoEvent(PlayerReceivedCards.class);

        List<Card> passedCards = threeCardsOfSuite(Suite.SPADES);
        passCards(player4(), passedCards);

        assertNoEvent(PlayerReceivedCards.class);

        passCards(player1(), threeCardsOfSuite(Suite.HEARTS));

        assertEvent(PlayerReceivedCards.class, event -> {
            assertThat(event.fromPlayer(), is(equalTo(player4())));
            assertThat(event.toPlayer(), is(equalTo(player1())));
            assertThat(event.cards(), is(equalTo(passedCards)));
        });
    }

    @Test
    void do_not_start_playing_until_all_cards_have_been_passed() {
        passCards(player1(), threeCardsOfSuite(Suite.HEARTS));
        passCards(player2(), threeCardsOfSuite(Suite.CLUBS));
        passCards(player3(), threeCardsOfSuite(Suite.DIAMONDS));

        assertNoEvent(StartedPlaying.class);
    }

    @Test
    void first_trick_opens_with_player_who_has_two_of_spades_when_all_cards_have_been_passed() {
        passCards(player1(), threeCardsOfSuite(Suite.HEARTS));
        passCards(player2(), threeCardsOfSuite(Suite.CLUBS));
        passCards(player3(), threeCardsOfSuite(Suite.DIAMONDS));
        passCards(player4(), threeCardsOfSuite(Suite.SPADES));

        assertEvent(StartedPlaying.class, event -> {
            assertThat(event.leadingPlayer(), is(equalTo(player2())));
        });
    }

    @Test
    void player_cannot_play_card_before_passing_cards() {
        assertThrows(NotPlayersTurn.class, () -> {
            playCard(player2(), twoOfClubs());
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
