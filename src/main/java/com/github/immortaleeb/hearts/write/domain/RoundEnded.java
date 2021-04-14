package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.Map;

public final class RoundEnded implements GameEvent {
    private final Map<PlayerId, Integer> scores;

    public RoundEnded(Map<PlayerId, Integer> scores) {
        this.scores = scores;
    }

    public Map<PlayerId, Integer> scores() {
        return scores;
    }
}
