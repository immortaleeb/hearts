package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

public final class TrickWon implements GameEvent {

    private final PlayerId playerId;

    public TrickWon(PlayerId playerId) {
        this.playerId = playerId;
    }

    public PlayerId wonBy() {
        return playerId;
    }

    @Override
    public String toString() {
        return "TrickWon{" + "playerId=" + playerId + '}';
    }

}
