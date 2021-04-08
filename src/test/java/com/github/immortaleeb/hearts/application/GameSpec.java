package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.infrastructure.FakeGameRepository;
import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

abstract class GameSpec {

    protected GameId gameId;
    protected FakeGameRepository gameRepository;
    protected CommandDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        gameId = GameId.generate();

        Game game = new Game(gameId);
        game.loadFromHistory(given());

        gameRepository = new FakeGameRepository(game);
        dispatcher = new CommandDispatcher(gameRepository);
    }

    protected abstract List<GameEvent> given();

    protected GameId startGameWith(List<PlayerId> players) {
        return dispatcher.dispatch(new StartGame(players));
    }

    protected void passCards(PlayerId fromPlayer, List<Card> cards) {
        dispatcher.dispatch(new PassCards(gameId, fromPlayer, cards));
    }

    protected void playCard(PlayerId player, Card card) {
        dispatcher.dispatch(new PlayCard(gameId, player, card));
    }

    protected  <T extends GameEvent> void assertEvent(Class<T> eventClass, Consumer<T> eventConsumer) {
        eventConsumer.accept(getSingleEvent(eventClass));
    }

    protected  <T extends GameEvent> void assertEvent(Class<T> eventClass) {
        T event = getSingleEvent(eventClass);
        assertThat(event, is(notNullValue()));
    }

    protected <T extends GameEvent> void assertNoEvent(Class<T> eventClass) {
        List<T> events = getEvents(eventClass);

        assertThat(events, is(empty()));
    }

    protected <T extends GameEvent> List<T> getEvents(Class<T> eventClass) {
        return getEvents(this.gameId, eventClass);
    }

    protected <T extends GameEvent> List<T> getEvents(GameId gameId, Class<T> eventClass) {
        return gameRepository.getEvents(gameId)
                .stream()
                .filter(eventClass::isInstance)
                .map(eventClass::cast)
                .collect(Collectors.toList());
    }

    protected <T extends GameEvent> T getSingleEvent(Class<T> eventClass) {
        return getSingleEvent(this.gameId, eventClass);
    }

    protected <T extends GameEvent> T getSingleEvent(GameId gameId, Class<T> eventClass) {
        List<T> raisedEvents = getEvents(gameId, eventClass);

        assertThat("Expected an event of type " + eventClass, raisedEvents, hasSize(1));
        return eventClass.cast(raisedEvents.get(0));
    }

}
