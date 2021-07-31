package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.Map;

public final record RoundEnded(GameId gameId, Map<PlayerId, Integer> scores) implements GameEvent {
}
