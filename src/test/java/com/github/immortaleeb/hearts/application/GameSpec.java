package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.infrastructure.InMemoryGameRepository;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

class GameSpec {

    protected InMemoryGameRepository gameRepository;
    protected CommandDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        gameRepository = new InMemoryGameRepository();
        dispatcher = new CommandDispatcher(gameRepository);
    }

    protected GameId startGameWith(List<PlayerId> players) {
        return dispatcher.dispatch(new StartGame(players));
    }

    protected void passCards(GameId gameId, PlayerId fromPlayer, List<Card> cards) {
        dispatcher.dispatch(new PassCards(gameId, fromPlayer, cards));
    }

    protected  <T extends GameEvent> void assertEvent(GameId gameId, Class<T> eventClass, Consumer<T> eventConsumer) {
        eventConsumer.accept(getSingleEvent(gameId, eventClass));
    }

    protected <T extends GameEvent> void assertNoEvent(GameId gameId, Class<T> eventClass) {
        List<T> events = getEvents(gameId, eventClass);

        assertThat(events, is(empty()));
    }

    protected <T extends GameEvent> List<T> getEvents(GameId gameId, Class<T> eventClass) {
        return gameRepository.getEvents(gameId)
                .stream()
                .filter(eventClass::isInstance)
                .map(eventClass::cast)
                .collect(Collectors.toList());
    }

    protected <T extends GameEvent> T getSingleEvent(GameId gameId, Class<T> eventClass) {
        List<T> raisedEvents = getEvents(gameId, eventClass);

        assertThat("Expected an event of type " + eventClass, raisedEvents, hasSize(1));
        return eventClass.cast(raisedEvents.get(0));
    }

}
