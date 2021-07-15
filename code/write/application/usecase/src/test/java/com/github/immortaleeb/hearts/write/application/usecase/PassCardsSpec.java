package com.github.immortaleeb.hearts.write.application.usecase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.immortaleeb.hearts.write.application.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.write.shared.IncorrectNumberOfCardsPassed;
import com.github.immortaleeb.hearts.write.shared.NoCardsNeedToBePassed;
import com.github.immortaleeb.hearts.write.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.write.shared.PlayerAlreadyPassedCards;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;
import com.github.immortaleeb.hearts.write.application.fixtures.CardFixtures;
import com.github.immortaleeb.hearts.write.application.fixtures.ScenarioFixtures;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PassCardsSpec {

    @DisplayName("when game has started")
    @Nested
    public class GameStarted extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.gameStartedWith(players);
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
            List<Card> cards = CardFixtures.threeCardsOfSuite(Suite.HEARTS);

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
            List<Card> cards = CardFixtures.threeCardsOfSuite(Suite.HEARTS);
            List<Card> otherCards = Arrays.asList(
                    Card.of(Suite.HEARTS, Rank.FIVE),
                    Card.of(Suite.HEARTS, Rank.SIX),
                    Card.of(Suite.HEARTS, Rank.SEVEN)
            );

            passCards(player1(), cards);
            assertThrows(PlayerAlreadyPassedCards.class, () -> passCards(player1(), otherCards));
        }

        @Test
        void player_receives_cards_after_passing_their_cards() {
            assertNoEvent(PlayerHasTakenPassedCards.class);

            List<Card> passedCards = CardFixtures.threeCardsOfSuite(Suite.SPADES);
            passCards(player4(), passedCards);

            assertNoEvent(PlayerHasTakenPassedCards.class);

            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));

            assertEvent(PlayerHasTakenPassedCards.class, event -> {
                assertThat(event.fromPlayer(), is(equalTo(player4())));
                assertThat(event.toPlayer(), is(equalTo(player1())));
                assertThat(event.cards(), is(equalTo(passedCards)));
            });
        }

        @Test
        void do_not_start_playing_until_all_cards_have_been_passed() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));

            assertNoEvent(StartedPlaying.class);
        }

        @Test
        void first_trick_opens_with_player_who_has_two_of_spades_when_all_cards_have_been_passed() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));
            passCards(player4(), CardFixtures.threeCardsOfSuite(Suite.SPADES));

            assertEvent(StartedPlaying.class, event -> {
                assertThat(event.leadingPlayer(), is(equalTo(player2())));
            });
        }

        @Test
        void player_cannot_play_card_before_passing_cards() {
            assertThrows(NotPlayersTurn.class, () -> {
                playCard(player2(), CardFixtures.twoOfClubs());
            });
        }
    }

    @DisplayName("when playing round 1")
    @Nested
    public class Round1 extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.gameStartedWith(players);
        }

        @Test
        void cards_are_passed_to_left() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player4(), CardFixtures.threeCardsOfSuite(Suite.SPADES));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));

            List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

            assertThat(passEvents.get(0).toPlayer(), is(equalTo(player2())));
            assertThat(passEvents.get(1).toPlayer(), is(equalTo(player1())));
            assertThat(passEvents.get(2).toPlayer(), is(equalTo(player4())));
            assertThat(passEvents.get(3).toPlayer(), is(equalTo(player3())));
        }
    }

    @DisplayName("when playing round 2")
    @Nested
    public class Round2 extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.playedRoundsWith(1, players);
        }

        @Test
        void cards_are_passed_to_right() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player4(), CardFixtures.threeCardsOfSuite(Suite.SPADES));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));

            List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

            assertThat(passEvents.get(0).toPlayer(), is(equalTo(player4())));
            assertThat(passEvents.get(1).toPlayer(), is(equalTo(player3())));
            assertThat(passEvents.get(2).toPlayer(), is(equalTo(player2())));
            assertThat(passEvents.get(3).toPlayer(), is(equalTo(player1())));
        }

        @Test
        void player_receives_cards_after_passing_their_cards() {
            assertNoEvent(PlayerHasTakenPassedCards.class);

            List<Card> passedCards = CardFixtures.threeCardsOfSuite(Suite.CLUBS);
            passCards(player2(), passedCards);

            assertNoEvent(PlayerHasTakenPassedCards.class);

            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));

            assertEvent(PlayerHasTakenPassedCards.class, event -> {
                assertThat(event.fromPlayer(), is(equalTo(player2())));
                assertThat(event.toPlayer(), is(equalTo(player1())));
                assertThat(event.cards(), is(equalTo(passedCards)));
            });
        }
    }

    @DisplayName("when playing round 3")
    @Nested
    public class Round3 extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.playedRoundsWith(2, players);
        }

        @Test
        void cards_are_passed_diagonally() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player4(), CardFixtures.threeCardsOfSuite(Suite.SPADES));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));

            List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

            assertThat(passEvents.get(0).toPlayer(), is(equalTo(player3())));
            assertThat(passEvents.get(1).toPlayer(), is(equalTo(player2())));
            assertThat(passEvents.get(2).toPlayer(), is(equalTo(player1())));
            assertThat(passEvents.get(3).toPlayer(), is(equalTo(player4())));
        }
    }

    @DisplayName("when playing round 4")
    @Nested
    public class Round4 extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.playedRoundsWith(3, players);
        }

        @Test
        void cards_cannot_be_passed() {
            assertThrows(NoCardsNeedToBePassed.class, () -> passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS)));
        }

        @Test
        void cards_can_be_played() {
            playCard(player2(), CardFixtures.twoOfClubs());

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.playedBy(), is(equalTo(player2())));
                MatcherAssert.assertThat(event.card(), CoreMatchers.is(equalTo(CardFixtures.twoOfClubs())));
            });
        }
    }

    @DisplayName("when playing round 5")
    @Nested
    public class Round5 extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.playedRoundsWith(4, players);
        }

        @Test
        void cards_are_passed_to_left() {
            passCards(player1(), CardFixtures.threeCardsOfSuite(Suite.HEARTS));
            passCards(player4(), CardFixtures.threeCardsOfSuite(Suite.SPADES));
            passCards(player3(), CardFixtures.threeCardsOfSuite(Suite.DIAMONDS));
            passCards(player2(), CardFixtures.threeCardsOfSuite(Suite.CLUBS));

            List<PlayerPassedCards> passEvents = getEvents(PlayerPassedCards.class);

            assertThat(passEvents.get(0).toPlayer(), is(equalTo(player2())));
            assertThat(passEvents.get(1).toPlayer(), is(equalTo(player1())));
            assertThat(passEvents.get(2).toPlayer(), is(equalTo(player4())));
            assertThat(passEvents.get(3).toPlayer(), is(equalTo(player3())));
        }
    }

}
