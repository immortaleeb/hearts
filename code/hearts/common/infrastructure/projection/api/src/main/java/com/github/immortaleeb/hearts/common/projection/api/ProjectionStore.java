package com.github.immortaleeb.hearts.common.projection.api;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hears.common.shared.PlayerHand;

import java.util.Optional;

public interface ProjectionStore {

    Optional<GameSummary> findById(GameId gameId);

    void save(GameSummary gameSummary);

    Optional<PlayerHand> findById(PlayerId playerId);

    void save(PlayerHand playerHand);

}
