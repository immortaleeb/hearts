package com.github.immortaleeb.hearts.common.infrastructure.projection.inmemory;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.GameSummary;
import com.github.immortaleeb.hears.common.shared.PlayerHand;
import com.github.immortaleeb.hearts.common.projection.api.ProjectionStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProjectionStore implements ProjectionStore {

    private final Map<GameId, GameSummary> gameSummariesById = new HashMap<>();
    private final Map<PlayerId, PlayerHand> playerHandsById = new HashMap<>();

    @Override
    public Optional<GameSummary> findById(GameId gameId) {
        return Optional.ofNullable(gameSummariesById.get(gameId));
    }

    @Override
    public void save(GameSummary gameSummary) {
        gameSummariesById.put(gameSummary.id(), gameSummary);
    }

    @Override
    public Optional<PlayerHand> findById(PlayerId playerId) {
        return Optional.ofNullable(playerHandsById.get(playerId));
    }

    @Override
    public void save(PlayerHand playerHand) {
        playerHandsById.put(playerHand.player(), playerHand);
    }

}
