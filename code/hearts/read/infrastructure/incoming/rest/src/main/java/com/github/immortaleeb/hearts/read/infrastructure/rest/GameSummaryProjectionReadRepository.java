package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.common.projection.api.ProjectionStore;

import java.util.Optional;

public class GameSummaryProjectionReadRepository implements GameSummaryReadRepository {

    private final ProjectionStore store;

    public GameSummaryProjectionReadRepository(ProjectionStore store) {
        this.store = store;
    }

    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return store.findById(gameId);
    }

}
