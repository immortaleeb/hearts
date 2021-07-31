package com.github.immortaleeb.hearts.write.application.infrastructure;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;

import java.util.Optional;

public class FakeGameSummaryWriteRepository implements GameSummaryWriteRepository {
    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return Optional.empty();
    }

    @Override
    public void save(GameSummary gameSummary) {
        // does nothing
    }
}
