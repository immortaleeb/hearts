package com.github.immortaleeb.lobby.shared;

import java.util.Objects;
import java.util.UUID;

public class GameId {
    private final UUID uuid;

    private GameId(UUID uuid) {
        this.uuid = uuid;
    }

    public String asString() {
        return uuid.toString();
    }

    public static GameId generate() {
        return new GameId(UUID.randomUUID());
    }

    public static GameId of(String gameId) {
        return new GameId(UUID.fromString(gameId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameId gameId = (GameId) o;
        return Objects.equals(uuid, gameId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return asString();
    }

}
