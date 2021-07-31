package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.PlayerHand;
import com.github.immortaleeb.hearts.common.projection.api.ProjectionStore;

import java.util.Optional;

public class PlayerHandProjectionReadRepository implements PlayerHandReadRepository {

    private final ProjectionStore projectionStore;

    public PlayerHandProjectionReadRepository(ProjectionStore projectionStore) {
        this.projectionStore = projectionStore;
    }

    @Override
    public Optional<PlayerHand> findById(PlayerId playerId) {
        return projectionStore.findById(playerId);
    }

}
