package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hearts.write.application.infrastructure.FakeGameRepository;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.application.fixtures.PlayerIdFixtures;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.application.util.Events;
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
    protected List<PlayerId> players;
    protected FakeGameRepository gameRepository;
    protected CommandHandlerRegistry registry;
    protected CommandHandlerDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        gameId = GameId.generate();
        players = PlayerIdFixtures.players();

        Game game = new Game(gameId);
        game.loadFromHistory(given().toList());

        gameRepository = new FakeGameRepository(game);
        registry = new CommandHandlerRegistry();
        dispatcher = new CommandHandlerDispatcher(registry);

        GameCommandHandlers.registerAll(registry, gameRepository);
    }

    protected abstract Events given();

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

    protected PlayerId player1() {
        return players.get(0);
    }

    protected PlayerId player2() {
        return players.get(1);
    }

    protected PlayerId player3() {
        return players.get(2);
    }

    protected PlayerId player4() {
        return players.get(3);
    }

}
