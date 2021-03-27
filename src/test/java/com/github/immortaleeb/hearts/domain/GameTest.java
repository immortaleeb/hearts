package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

class GameTest {

    private List<PlayerId> players;
    private Game game;

    @BeforeEach
    void setUp() {
        players = PlayerIdFixtures.players();
        game = Game.startWith(players);
    }

    @Test
    void startWith_starts_a_game() {
        assertEvent(game, GameStarted.class, event -> {
            assertThat(event.players(), is(equalTo(players)));
        });
    }

    @Test
    void startWith_deals_13_cards_to_each_player() {
        assertEvent(game, CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();

            assertEachHand(playerHands, hasSize(13));
        });
    }

    @Test
    void startWith_deals_unique_cards_to_each_player() {
        assertEvent(game, CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();
            List<Card> allCards = playerHands.values().stream().flatMap(List::stream).collect(Collectors.toList());

            assertEachHand(playerHands, hasUniqueItems());
            assertThat(allCards, hasUniqueItems());
        });
    }

    @Test
    void startWith_deals_shuffled_cards() {
        Game otherGame = Game.startWith(players);

        Map<PlayerId, List<Card>> gamePlayerHands = getSingleEvent(game, CardsDealt.class).playerHands();
        Map<PlayerId, List<Card>> otherGamePlayerHands = getSingleEvent(otherGame, CardsDealt.class).playerHands();

        assertThat(gamePlayerHands, is(not(equalTo(otherGamePlayerHands))));
    }

    // helper methods

    private void assertEachHand(Map<PlayerId, List<Card>> playerHands, Matcher<Collection<? extends Card>> matcher) {
        assertThat(playerHands.get(players.get(0)), matcher);
        assertThat(playerHands.get(players.get(1)), matcher);
        assertThat(playerHands.get(players.get(2)), matcher);
        assertThat(playerHands.get(players.get(3)), matcher);
    }

    private <T extends GameEvent> void assertEvent(Game game, Class<T> eventClass, Consumer<T> eventConsumer) {
        eventConsumer.accept(getSingleEvent(game, eventClass));
    }

    private <T extends GameEvent> T getSingleEvent(Game game, Class<T> eventClass) {
        List<T> raisedEvents = game.raisedEvents()
                .stream()
                .filter(eventClass::isInstance)
                .map(eventClass::cast)
                .collect(Collectors.toList());

        assertThat("Expected an event of type " + eventClass, raisedEvents, hasSize(1));
        return eventClass.cast(raisedEvents.get(0));
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