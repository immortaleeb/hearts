package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;

import java.util.Optional;

public interface GameSummaryReadRepository {

    Optional<GameSummary> findById(GameId gameId);

}
