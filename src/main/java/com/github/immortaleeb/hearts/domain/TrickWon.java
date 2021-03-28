package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.PlayerId;

public class TrickWon implements GameEvent {

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
