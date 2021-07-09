package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.Map;

public final record RoundEnded(Map<PlayerId, Integer> scores) implements GameEvent {
}
