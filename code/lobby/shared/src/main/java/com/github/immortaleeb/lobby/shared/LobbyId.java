package com.github.immortaleeb.lobby.shared;

import java.util.UUID;

public class LobbyId {

    private final String lobbyId;

    private LobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String asString() {
        return lobbyId;
    }

    @Override
    public String toString() {
        return asString();
    }

    public static LobbyId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid lobby id");
        }

        return new LobbyId(value);
    }

    public static LobbyId generate() {
        return of(UUID.randomUUID().toString());
    }

}
