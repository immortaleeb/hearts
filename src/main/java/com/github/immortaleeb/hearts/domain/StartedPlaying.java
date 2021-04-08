package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.PlayerId;

public class StartedPlaying implements GameEvent {

    private final PlayerId leadingPlayer;

    public StartedPlaying(PlayerId leadingPlayer) {
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
