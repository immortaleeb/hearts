package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.PlayerId;

public class RoundStarted implements GameEvent {

    private final PlayerId leadingPlayer;

    public RoundStarted(PlayerId leadingPlayer) {
        this.leadingPlayer = leadingPlayer;
    }

    public PlayerId leadingPlayer() {
        return leadingPlayer;
    }

    @Override
    public String toString() {
        return "RoundStarted{" + "leadingPlayer=" + leadingPlayer + '}';
    }

}
