package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.PlayerHand;

import java.util.Optional;

public interface PlayerHandWriteRepository {

    Optional<PlayerHand> findById(PlayerId playerId);

    void save(PlayerHand hand);

}
