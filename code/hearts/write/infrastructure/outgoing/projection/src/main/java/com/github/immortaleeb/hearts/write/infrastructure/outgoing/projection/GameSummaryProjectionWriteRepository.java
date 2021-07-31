package com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.common.projection.api.ProjectionStore;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;

import java.util.Optional;

public class GameSummaryProjectionWriteRepository implements GameSummaryWriteRepository {

    private final ProjectionStore projectionStore;

    public GameSummaryProjectionWriteRepository(ProjectionStore projectionStore) {
        this.projectionStore = projectionStore;
    }

    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return projectionStore.findById(gameId);
    }

    @Override
    public void save(GameSummary gameSummary) {
        projectionStore.save(gameSummary);
    }

}
