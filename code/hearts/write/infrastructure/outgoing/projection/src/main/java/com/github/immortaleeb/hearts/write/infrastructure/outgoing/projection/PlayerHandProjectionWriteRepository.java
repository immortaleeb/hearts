package com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.PlayerHand;
import com.github.immortaleeb.hearts.common.projection.api.ProjectionStore;
import com.github.immortaleeb.hearts.write.domain.PlayerHandWriteRepository;

import java.util.Optional;

public class PlayerHandProjectionWriteRepository implements PlayerHandWriteRepository {
    private final ProjectionStore projectionStore;

    public PlayerHandProjectionWriteRepository(ProjectionStore projectionStore) {
        this.projectionStore = projectionStore;
    }

    @Override
    public Optional<PlayerHand> findById(PlayerId playerId) {
        return projectionStore.findById(playerId);
    }

    @Override
    public void save(PlayerHand hand) {
        projectionStore.save(hand);
    }

}
