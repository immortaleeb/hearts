package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.domain.CardsDealt;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.domain.GameStarted;
import com.github.immortaleeb.hearts.infrastructure.InMemoryGameRepository;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

class StartGameSpec {

    private InMemoryGameRepository gameRepository;
    private CommandDispatcher dispatcher;

    private List<PlayerId> players;
    private GameId gameId;

    @BeforeEach
    void setUp() {
        gameRepository = new InMemoryGameRepository();
        players = PlayerIdFixtures.players();

        dispatcher = new CommandDispatcher(gameRepository);
        gameId = startGameWith(players);
    }

    @Test
    void startGame_generates_a_game_id() {
        assertThat(gameId, is(notNullValue()));
    }

    @Test
    void startGame_starts_a_game() {
        assertEvent(gameId, GameStarted.class, event -> {
            MatcherAssert.assertThat(event.players(), is(equalTo(players)));
        });
    }

    @Test
    void startGame_deals_13_cards_to_each_player() {
        assertEvent(gameId, CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();

            assertEachHand(playerHands, hasSize(13));
        });
    }

    @Test
    void startGame_deals_unique_cards_to_each_player() {
        assertEvent(gameId, CardsDealt.class, event -> {
            Map<PlayerId, List<Card>> playerHands = event.playerHands();
            List<Card> allCards = playerHands.values().stream().flatMap(List::stream).collect(Collectors.toList());

            assertEachHand(playerHands, hasUniqueItems());
            assertThat(allCards, hasUniqueItems());
        });
    }

    @Test
    void startGame_deals_shuffled_cards() {
        GameId otherGameId = startGameWith(players);

        Map<PlayerId, List<Card>> gamePlayerHands = getSingleEvent(gameId, CardsDealt.class).playerHands();
        Map<PlayerId, List<Card>> otherGamePlayerHands = getSingleEvent(otherGameId, CardsDealt.class).playerHands();

        assertThat(gamePlayerHands, is(not(equalTo(otherGamePlayerHands))));
    }

    // helper methods

    private GameId startGameWith(List<PlayerId> players) {
        return dispatcher.dispatch(new StartGame(players));
    }

    private void assertEachHand(Map<PlayerId, List<Card>> playerHands, Matcher<Collection<? extends Card>> matcher) {
        assertThat(playerHands.get(players.get(0)), matcher);
        assertThat(playerHands.get(players.get(1)), matcher);
        assertThat(playerHands.get(players.get(2)), matcher);
        assertThat(playerHands.get(players.get(3)), matcher);
    }

    private <T extends GameEvent> void assertEvent(GameId gameId, Class<T> eventClass, Consumer<T> eventConsumer) {
        eventConsumer.accept(getSingleEvent(gameId, eventClass));
    }

    private <T extends GameEvent> T getSingleEvent(GameId gameId, Class<T> eventClass) {
        List<T> raisedEvents = gameRepository.getEvents(gameId)
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