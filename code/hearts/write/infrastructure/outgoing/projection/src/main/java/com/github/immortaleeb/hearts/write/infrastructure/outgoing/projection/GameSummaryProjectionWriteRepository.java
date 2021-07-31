package com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.common.projection.api.GameSummaryStore;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;

import java.util.Optional;

public class GameSummaryProjectionWriteRepository implements GameSummaryWriteRepository {

    private final GameSummaryStore gameSummaryStore;

    public GameSummaryProjectionWriteRepository(GameSummaryStore gameSummaryStore) {
        this.gameSummaryStore = gameSummaryStore;
    }

    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return gameSummaryStore.findById(gameId);
    }

    @Override
    public void save(GameSummary gameSummary) {
        gameSummaryStore.save(gameSummary);
    }

}
