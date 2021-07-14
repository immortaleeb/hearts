package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.shared.GameId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryEventStore implements EventStore {

    private final Map<GameId, List<GameEvent>> allEvents;
    private final EventDispatcher eventDispatcher;

    public InMemoryEventStore(EventDispatcher eventDispatcher) {
        this.allEvents = new HashMap<>();
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public List<GameEvent> loadAll(GameId gameId) {
        return allEvents.getOrDefault(gameId, new ArrayList<>());
    }

    @Override
    public void store(GameId gameId, List<GameEvent> events) {
        List<GameEvent> storedEvents = allEvents.computeIfAbsent(gameId, id -> new ArrayList<>());
        storedEvents.addAll(events);

        eventDispatcher.dispatchAll(events);
    }

}
