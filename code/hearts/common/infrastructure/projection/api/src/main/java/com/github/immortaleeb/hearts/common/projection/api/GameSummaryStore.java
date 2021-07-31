package com.github.immortaleeb.hearts.common.projection.api;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;

import java.util.Optional;

public interface GameSummaryStore {

    Optional<GameSummary> findById(GameId gameId);

    void save(GameSummary gameSummary);

}
