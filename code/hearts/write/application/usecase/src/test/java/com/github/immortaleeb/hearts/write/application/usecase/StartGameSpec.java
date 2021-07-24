package com.github.immortaleeb.hearts.write.application.usecase;

import static com.github.immortaleeb.hearts.write.application.fixtures.PlayerIdFixtures.generatePlayers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.immortaleeb.hearts.write.application.fixtures.PlayerIdFixtures;
import com.github.immortaleeb.hearts.write.application.util.Events;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.WrongNumberOfPlayers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StartGameSpec extends GameSpec {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        when();
    }

    @Override
    protected Events given() {
        return Events.none();
    }

    private void when() {
        gameId = startGameWith(players);
    }

    @Test
    void startGame_generates_a_game_id() {
        assertThat(gameId, is(notNullValue()));
    }

    @Test
    void startGame_starts_a_game() {
        assertEvent(GameStarted.class, event -> {
            MatcherAssert.assertThat(event.players(), is(equalTo(players)));
        });
    }

    @Test
    void startGame_deals_13_cards_to_each_player() {
        assertEvent(CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();

            assertEachHand(playerHands, hasSize(13));
        });
    }

    @Test
    void startGame_deals_unique_cards_to_each_player() {
        assertEvent(CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();
            List<Card> allCards = playerHands.values().stream().flatMap(List::stream).collect(Collectors.toList());

            assertEachHand(playerHands, hasUniqueItems());
            assertThat(allCards, hasUniqueItems());
        });
    }

    @Test
    void startGame_deals_shuffled_cards() {
        GameId otherGameId = startGameWith(players);

        Map<PlayerId, List<Card>> gamePlayerHands = getSingleEvent(CardsDealt.class).playerHands();
        Map<PlayerId, List<Card>> otherGamePlayerHands = getSingleEvent(otherGameId, CardsDealt.class).playerHands();

        assertThat(gamePlayerHands, is(not(equalTo(otherGamePlayerHands))));
    }

    @Test
    void startGame_fails_when_player_count_is_less_than_4() {
        List<PlayerId> players = generatePlayers(3);

        assertThrows(WrongNumberOfPlayers.class, () -> startGameWith(players));
    }

    @Test
    void startGame_fails_when_player_count_is_more_than_4() {
        List<PlayerId> players = generatePlayers(5);

        assertThrows(WrongNumberOfPlayers.class, () -> startGameWith(players));
    }

    // helper methods

    private void assertEachHand(Map<PlayerId, List<Card>> playerHands, Matcher<Collection<? extends Card>> matcher) {
        assertThat(playerHands.get(player1()), matcher);
        assertThat(playerHands.get(player2()), matcher);
        assertThat(playerHands.get(player3()), matcher);
        assertThat(playerHands.get(player4()), matcher);
    }

    private Matcher<Collection<? extends Card>> hasUniqueItems() {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Collection<? extends Card> cards) {
                HashSet<Card> cardSet = new HashSet<>(cards);
                return cardSet.size() == cards.size();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a list with unique elements");
            }
        };
    }

}