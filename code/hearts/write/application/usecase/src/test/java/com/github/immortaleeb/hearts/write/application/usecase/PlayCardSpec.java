package com.github.immortaleeb.hearts.write.application.usecase;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.immortaleeb.hearts.write.application.scenarios.RoundScenario;
import com.github.immortaleeb.hearts.write.application.scenarios.RoundScenarioEvents;
import com.github.immortaleeb.hearts.write.application.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameEnded;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.CardNotInHand;
import com.github.immortaleeb.hearts.write.shared.InvalidCardPlayed;
import com.github.immortaleeb.hearts.write.shared.NotPlayersTurn;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;
import com.github.immortaleeb.hearts.write.application.fixtures.CardFixtures;
import com.github.immortaleeb.hearts.write.application.fixtures.ScenarioFixtures;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class PlayCardSpec {

    @Nested
    @DisplayName("given no cards have been played yet")
    public class GivenNoCardsPlayed extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players);
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
        void leading_player_must_open_round_with_two_of_clubs() {
            InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE)));

            assertThat(exception.getMessage(), is(equalTo("You must open with the two of clubs")));
        }

        @Test
        void player2_opens_with_two_of_clubs() {
            whenTwoOfClubsIsPlayed();

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.playedBy(), is(equalTo(player2())));
                assertThat(event.card(), CoreMatchers.is(equalTo(CardFixtures.twoOfClubs())));
            });
        }

        @Test
        void playing_a_card_moves_leading_player_to_player_3_after_player_2() {
            whenTwoOfClubsIsPlayed();

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.nextLeadingPlayer().get(), is(equalTo(player3())));
            });
        }

        @Test
        void playing_a_card_includes_valid_cards_to_play_for_player3() {
            whenTwoOfClubsIsPlayed();

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.validCardsForNextPlayer(), is(equalTo(List.of(
                        Card.of(Suite.CLUBS, Rank.TEN),
                        Card.of(Suite.CLUBS, Rank.JACK),
                        Card.of(Suite.CLUBS, Rank.ACE)
                ))));
            });
        }

        private void whenTwoOfClubsIsPlayed() {
            playCard(player2(), CardFixtures.twoOfClubs());
        }
    }

    @Nested
    @DisplayName("given player 2 played two of clubs")
    public class GivenPlayer2PlayedTwoOfClubs extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .add(new CardPlayed(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO), player3(), emptyList()));
        }

        @Test
        void player_cannot_play_card_twice_in_a_row() {
            assertThrows(NotPlayersTurn.class, () -> playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE)));
        }

        @Test
        void player_cannot_play_when_its_not_their_turn() {
            assertThrows(NotPlayersTurn.class, () -> playCard(player4(), Card.of(Suite.DIAMONDS, Rank.TWO)));
        }

        @Test
        void leading_player_cannot_play_any_card_that_they_passed_to_another_player() {
            List<Card> passedCards = CardFixtures.threeCardsOfSuite(Suite.DIAMONDS);

            assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(0)));
            assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(1)));
            assertThrows(CardNotInHand.class, () -> playCard(player3(), passedCards.get(2)));
        }

        @Test
        void leading_player_can_play_a_card_they_received_from_another_player() {
            // given
            List<Card> receivedCards = CardFixtures.threeCardsOfSuite(Suite.CLUBS);
            Card receivedCard = receivedCards.get(0);

            // when
            playCard(player3(), receivedCard);

            // then
            assertEvent(CardPlayed.class, event -> {
                assertThat(event.card(), is(equalTo(receivedCard)));
            });
        }

        @Test
        void leading_player_must_follow_suite() {
            InvalidCardPlayed exception = assertThrows(InvalidCardPlayed.class,
                () -> playCard(player3(), Card.of(Suite.DIAMONDS, Rank.TWO)));

            assertThat(exception.getMessage(), is(equalTo("Player must follow suite when possible")));
        }

        @Test
        void playing_a_card_moves_leading_player_to_player_4() {
            playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN));

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.nextLeadingPlayer().get(), is(equalTo(player4())));
            });
        }
    }

    @Nested
    @DisplayName("given 2 cards were played")
    public class Given2CardsWerePlayed extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .add(new CardPlayed(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO), player3(), emptyList()))
                .add(new CardPlayed(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN), player4(), emptyList()));
        }

        @Test
        void trick_is_not_won_until_all_players_played_cards() {
            // when
            whenThirdCardIsPlayed();

            // then
            assertNoEvent(TrickWon.class);
        }

        private void whenThirdCardIsPlayed() {
            playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));
        }
    }

    @Nested
    @DisplayName("given 3 cards were played")
    public class Given3CardsWerePlayed extends GameSpec {

        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .add(new CardPlayed(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO), player3(), emptyList()))
                .add(new CardPlayed(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN), player4(), emptyList()))
                .add(new CardPlayed(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO), player1(), emptyList()));
        }

        @Test
        void trick_is_won_when_all_players_played_cards() {
            whenFourthCardIsPlayed();

            assertEvent(TrickWon.class);
        }

        @Test
        void player_with_highest_card_following_trick_suite_wins_trick() {
            whenFourthCardIsPlayed();

            assertEvent(TrickWon.class, event -> {
                assertThat(event.wonBy(), is(equalTo(player3())));
            });
        }

        @Test
        void next_leading_player_is_empty_when_trick_is_finished() {
            whenFourthCardIsPlayed();

            assertEvent(CardPlayed.class, event -> {
                assertThat(event.nextLeadingPlayer().isEmpty(), is(equalTo(true)));
            });
        }

        private void whenFourthCardIsPlayed() {
            playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));
        }

    }

    @Nested
    @DisplayName("given first trick played")
    public class GivenFirstTrickPlayed extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .add(new CardPlayed(gameId, player2(), Card.of(Suite.CLUBS, Rank.TWO), player3(), emptyList()))
                .add(new CardPlayed(gameId, player3(), Card.of(Suite.CLUBS, Rank.TEN), player4(), emptyList()))
                .add(new CardPlayed(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO), player1(), emptyList()))
                .add(new CardPlayed(gameId, player1(), Card.of(Suite.HEARTS, Rank.TWO), null, emptyList()))
                .add(new TrickWon(gameId, player3()));
        }

        @Test
        void player_who_won_the_trick_is_the_next_leading_player() {
            assertThrows(NotPlayersTurn.class, () -> playCard(player1(), Card.of(Suite.HEARTS, Rank.THREE)));
            assertThrows(NotPlayersTurn.class, () -> playCard(player2(), Card.of(Suite.CLUBS, Rank.THREE)));
            assertThrows(NotPlayersTurn.class, () -> playCard(player4(), Card.of(Suite.SPADES, Rank.THREE)));

            playCard(player3(), Card.of(Suite.DIAMONDS, Rank.THREE));

            assertThat(getEvents(CardPlayed.class), hasSize(1));
        }

        @Test
        void leading_player_cannot_play_already_played_card() {
            assertThrows(CardNotInHand.class, () -> playCard(player3(), Card.of(Suite.CLUBS, Rank.TEN)));
        }
    }

    @Nested
    @DisplayName("given 12 tricks and 2 cards played")
    public class Given12TricksAnd2CardsPlayed extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .addAll(ScenarioFixtures.playRegular12Tricks(players))
                .addAll(
                    new CardPlayed(gameId, player2(), Card.of(Suite.HEARTS, Rank.TEN), player3(), emptyList()),
                    new CardPlayed(gameId, player3(), Card.of(Suite.DIAMONDS, Rank.TWO), player4(), emptyList())
                );
        }

        @Test
        void round_does_not_end_until_all_players_have_played_13_cards() {
            playCard(player4(), Card.of(Suite.SPADES, Rank.TWO));

            assertNoEvent(RoundEnded.class);
        }
    }

    @Nested
    @DisplayName("given 12 tricks and 3 cards played in regular round")
    public class Given12TricksAnd3CardsPlayed extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .addAll(ScenarioFixtures.playRegular12Tricks(players))
                .addAll(
                    new CardPlayed(gameId, player2(), Card.of(Suite.HEARTS, Rank.TEN), player3(), emptyList()),
                    new CardPlayed(gameId, player3(), Card.of(Suite.DIAMONDS, Rank.TWO), player4(), emptyList()),
                    new CardPlayed(gameId, player4(), Card.of(Suite.SPADES, Rank.TWO), player1(), emptyList())
                );
        }

        @Test
        void round_ends_when_all_players_have_played_13_cards() {
            whenLastCardIsPlayed();

            assertEvent(RoundEnded.class);
        }

        @Test
        void round_ends_with_correct_scores() {
            whenLastCardIsPlayed();

            assertEvent(RoundEnded.class, event -> {
                Map<PlayerId, Integer> scores = event.scores();
                assertThat(scores.get(player1()), is(equalTo(0)));
                assertThat(scores.get(player2()), is(equalTo(25)));
                assertThat(scores.get(player3()), is(equalTo(1)));
                assertThat(scores.get(player4()), is(equalTo(0)));
            });
        }

        @Test
        void cards_are_dealt_for_round_2_after_round_1_has_ended() {
            whenLastCardIsPlayed();

            assertEvent(CardsDealt.class, event -> {
                assertThat(event.playerHands().get(player1()), hasSize(13));
                assertThat(event.playerHands().get(player2()), hasSize(13));
                assertThat(event.playerHands().get(player3()), hasSize(13));
                assertThat(event.playerHands().get(player4()), hasSize(13));
            });
        }

        private void whenLastCardIsPlayed() {
            playCard(player1(), Card.of(Suite.SPADES, Rank.JACK));
        }
    }

    @Nested
    @DisplayName("given 12 tricks and 3 cards played when player is shooting for the moon")
    public class Given12TricksAnd3CardsPlayedWhenPlayerIsShootingForTheMoon extends GameSpec {
        @Override
        protected Events given() {
            return ScenarioFixtures.startedPlayingCardsWith(players)
                .addAll(ScenarioFixtures.play12TricksAnd3CardsofShootForTheMoonRound(players));
        }

        @Test
        void player_who_shot_for_the_moon_has_score_minus_26_when_round_ends() {
            whenLastCardIsPlayed();

            assertEvent(RoundEnded.class, event -> {
                Map<PlayerId, Integer> scores = event.scores();
                assertThat(scores.get(player1()), is(equalTo(0)));
                assertThat(scores.get(player2()), is(equalTo(-26)));
                assertThat(scores.get(player3()), is(equalTo(0)));
                assertThat(scores.get(player4()), is(equalTo(0)));
            });
        }

        private void whenLastCardIsPlayed() {
            playCard(player1(), Card.of(Suite.HEARTS, Rank.TWO));
        }
    }

    @Nested
    @DisplayName("given 2 rounds, 12 tricks and 3 cards played")
    public class Given2RoundsAnd12TricksAnd3CardsPlayed extends GameSpec {

        private RoundScenario round3;

        @Override
        protected Events given() {
            round3 = ScenarioFixtures.regularScenarioForRound(2).apply(players);
            RoundScenarioEvents round3Events = RoundScenarioEvents.eventsFor(round3);

            return ScenarioFixtures.startedPlayingCardsWith(players)
                .addAll(ScenarioFixtures.playedRoundsWith(2, players))
                .addAll(round3Events.eventsForCardsPassed(gameId))
                .addAll(round3Events.eventsForFirst12Tricks())
                .addAll(round3Events.eventsForFirst3CardsOfTrick(13));
        }

        @Test
        void round_ends_with_correct_scores_when_last_card_is_played() {
            whenLastCardIsPlayed();

            assertEvent(RoundEnded.class, event -> {
                Map<PlayerId, Integer> scores = event.scores();
                assertThat(scores.get(player1()), is(equalTo(0)));
                assertThat(scores.get(player2()), is(equalTo(11)));
                assertThat(scores.get(player3()), is(equalTo(0)));
                assertThat(scores.get(player4()), is(equalTo(15)));
            });
        }

        private void whenLastCardIsPlayed() {
            CardPlayed lastCardPlayed = round3.trick(13).lastCardPlayed();
            playCard(lastCardPlayed.playedBy(), lastCardPlayed.card());
        }

    }

    @Nested
    @DisplayName("given 7 rounds, 12 tricks and 3 cards played")
    public class Given7RoundsAnd12TricksAnd3CardsPlayed extends GameSpec {

        private RoundScenario round8;

        @Override
        protected Events given() {
            round8 = ScenarioFixtures.regularScenarioForRound(7).apply(players);
            RoundScenarioEvents round8Events = RoundScenarioEvents.eventsFor(round8);

            return ScenarioFixtures.startedPlayingCardsWith(players)
                .addAll(ScenarioFixtures.playedRoundsWith(7, players))
                .addAll(round8Events.eventsForCardsPassed(gameId))
                .addAll(round8Events.eventsForFirst12Tricks())
                .addAll(round8Events.eventsForFirst3CardsOfTrick(13));
        }

        @Test
        void game_ends_with_total_scores_when_last_card_is_played() {
            givenLastCardPlayed();

            assertEvent(GameEnded.class, roundEnded -> {
                assertThat(roundEnded.scores().get(player1()), is(equalTo(50)));
                assertThat(roundEnded.scores().get(player2()), is(equalTo(124)));
                assertThat(roundEnded.scores().get(player3()), is(equalTo(4)));
                assertThat(roundEnded.scores().get(player4()), is(equalTo(30)));
            });
        }

        @Test
        void game_ends_when_round_is_finished_and_player_reaches_score_over_100() {
            givenLastCardPlayed();

            assertEvent(GameEnded.class);
        }

        @Test
        void no_cards_are_dealt_when_game_has_ended() {
            givenLastCardPlayed();

            assertNoEvent(CardsDealt.class);
        }

        private void givenLastCardPlayed() {
            CardPlayed lastCardPlayed = round8.trick(13).lastCardPlayed();
            playCard(lastCardPlayed.playedBy(), lastCardPlayed.card());
        }
    }

}
