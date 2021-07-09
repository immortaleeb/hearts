package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.shared.GameId;

public class InMemoryGameRepository implements GameRepository {

    private final EventStore eventStore;

    public InMemoryGameRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Game load(GameId gameId) {
        Game game = new Game(gameId);

        for (GameEvent event : eventStore.loadAll(gameId)) {
            game.apply(event);
        }

        return game;
    }

    @Override
    public void save(Game game) {
        eventStore.store(game.id(), game.raisedEvents());
    }

}
