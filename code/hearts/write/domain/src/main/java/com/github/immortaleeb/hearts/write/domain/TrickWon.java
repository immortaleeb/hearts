package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

public record TrickWon(GameId gameId, PlayerId wonBy) implements GameEvent {
}
