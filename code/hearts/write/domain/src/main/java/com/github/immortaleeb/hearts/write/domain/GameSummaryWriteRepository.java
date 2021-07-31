package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;

import java.util.Optional;

public interface GameSummaryWriteRepository {

    Optional<GameSummary> findById(GameId gameId);

    void save(GameSummary gameSummary);

}
