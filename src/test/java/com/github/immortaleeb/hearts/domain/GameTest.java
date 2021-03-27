package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.PlayerIdFixtures;
import com.github.immortaleeb.hearts.shared.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

class GameTest {

    @Test
    void startWith_starts_a_game() {
        List<PlayerId> players = PlayerIdFixtures.players();
        Game game = Game.startWith(players);

        assertEvent(game, GameStarted.class, event -> {
            assertThat(event.players(), is(equalTo(players)));
        });
    }

    // helper methods

    public <T extends GameEvent> void assertEvent(Game game, Class<T> eventClass, Consumer<T> eventConsumer) {
        List<GameEvent> raisedEvents = game.raisedEvents();
        GameEvent raisedEvent = raisedEvents.get(0);

        assertThat(raisedEvent, is(instanceOf(eventClass)));
        eventConsumer.accept(eventClass.cast(raisedEvent));
    }

}