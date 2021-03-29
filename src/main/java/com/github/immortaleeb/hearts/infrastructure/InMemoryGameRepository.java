package com.github.immortaleeb.hearts.infrastructure;

import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameEvent;
import com.github.immortaleeb.hearts.domain.GameRepository;
import com.github.immortaleeb.hearts.shared.GameId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryGameRepository implements GameRepository {

    public static final boolean ENABLE_PRINTS = true;
    public static final boolean DISABLE_PRINTS = false;

    private final Map<GameId, List<GameEvent>> raisedEvents = new HashMap<>();

    private boolean printEnabled;

    public InMemoryGameRepository() {
        this(DISABLE_PRINTS);
    }

    public InMemoryGameRepository(boolean printEnabled) {
        this.printEnabled = printEnabled;
    }

    @Override
    public Game load(GameId gameId) {
        Game game = new Game(gameId);

        print("=== load");
        for (GameEvent event : getEvents(gameId)) {
            game.apply(event);
            print(event);
        }
        return game;
    }

    @Override
    public void save(Game game) {
        List<GameEvent> events = raisedEvents.computeIfAbsent(game.id(), id -> new ArrayList<>());
        events.addAll(game.raisedEvents());
    }

    public List<GameEvent> getEvents(GameId gameId) {
        return raisedEvents.get(gameId);
    }

    private void print(Object message) {
        if (printEnabled) {
            System.out.println(message);
        }
    }

}
