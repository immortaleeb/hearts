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

    private final Map<GameId, List<GameEvent>> raisedEvents = new HashMap<>();

    @Override
    public void save(Game game) {
        List<GameEvent> events = raisedEvents.computeIfAbsent(game.id(), id -> new ArrayList<>());
        events.addAll(game.raisedEvents());
    }

    public List<GameEvent> getEvents(GameId gameId) {
        return raisedEvents.get(gameId);
    }

}
