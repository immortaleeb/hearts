package com.github.immortaleeb.hearts.common.infrastructure.projection.inmemory;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.common.projection.api.GameSummaryStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGameSummaryStore implements GameSummaryStore {

    private final Map<GameId, GameSummary> gameSummariesById = new HashMap<>();

    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return Optional.ofNullable(gameSummariesById.get(gameId));
    }

    @Override
    public void save(GameSummary gameSummary) {
        gameSummariesById.put(gameSummary.id(), gameSummary);
    }

}
