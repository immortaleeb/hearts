package com.github.immortaleeb.hearts.write.infrastructure.eventstore.api;

import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.List;

public interface EventStore {

    List<GameEvent> loadAll(GameId gameId);

    void store(GameId gameId, List<GameEvent> events);

}
