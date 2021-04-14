package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

public final class StartedPlaying implements GameEvent {

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
