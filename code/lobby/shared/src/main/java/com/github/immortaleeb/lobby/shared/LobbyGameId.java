package com.github.immortaleeb.lobby.shared;

import java.util.Objects;
import java.util.UUID;

public class LobbyGameId {
    private final UUID uuid;

    private LobbyGameId(UUID uuid) {
        this.uuid = uuid;
    }

    public String asString() {
        return uuid.toString();
    }

    public static LobbyGameId generate() {
        return new LobbyGameId(UUID.randomUUID());
    }

    public static LobbyGameId of(String gameId) {
        return new LobbyGameId(UUID.fromString(gameId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LobbyGameId gameId = (LobbyGameId) o;
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
