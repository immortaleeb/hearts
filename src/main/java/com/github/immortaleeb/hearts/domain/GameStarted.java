package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;

public final class GameStarted implements GameEvent {
    private final List<PlayerId> players;

    public GameStarted(List<PlayerId> players) {
        this.players = players;
    }

    public List<PlayerId> players() {
        return players;
    }

    @Override
    public String toString() {
        return "GameStarted{" + "players=" + players + '}';
    }

}
