package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

public record StartedPlaying(GameId gameId, PlayerId leadingPlayer) implements GameEvent {
}
