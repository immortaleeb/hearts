package com.github.immortaleeb.hearts.write.application.infrastructure;

import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameRepository implements GameRepository {

    private final Map<GameId, Game> games = new HashMap<>();
    private final Map<GameId, List<GameEvent>> raisedEventsMap = new HashMap<>();

    public FakeGameRepository(Game game) {
        this.games.put(game.id(), game);
    }

    @Override
    public Game load(GameId gameId) {
        return games.get(gameId);
    }

    @Override
    public void save(Game game) {
        List<GameEvent> raisedEvents = getEvents(game.id());
        raisedEvents.addAll(game.raisedEvents());
        raisedEventsMap.put(game.id(), raisedEvents);

        game.clearRaisedEvents();
    }

    public List<GameEvent> getEvents(GameId gameId) {
        return raisedEventsMap.getOrDefault(gameId, new ArrayList<>());
    }

}