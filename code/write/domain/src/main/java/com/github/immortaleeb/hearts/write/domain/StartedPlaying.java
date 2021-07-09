package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

public record StartedPlaying(GameId gameId, PlayerId leadingPlayer) implements GameEvent {
}
