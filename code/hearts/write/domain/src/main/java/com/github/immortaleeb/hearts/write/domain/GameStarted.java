package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;

public final class GameStarted implements GameEvent {
    private final GameId gameId;
    private final List<PlayerId> players;

    public GameStarted(GameId gameId, List<PlayerId> players) {
        this.gameId = gameId;
        this.players = players;
    }

    public GameId gameId() {
        return gameId;
    }

    public List<PlayerId> players() {
        return players;
    }

    @Override
    public String toString() {
        return "GameStarted{" + "gameId=" + gameId + ", players=" + players + '}';
    }
}
