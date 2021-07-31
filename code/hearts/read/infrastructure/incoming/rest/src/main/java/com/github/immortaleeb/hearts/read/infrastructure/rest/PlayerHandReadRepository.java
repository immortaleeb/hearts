package com.github.immortaleeb.hearts.read.infrastructure.rest;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.PlayerHand;

import java.util.Optional;

public interface PlayerHandReadRepository {

    Optional<PlayerHand> findById(PlayerId playerId);

}
