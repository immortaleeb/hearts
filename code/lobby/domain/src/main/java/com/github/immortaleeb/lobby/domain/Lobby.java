package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public class Lobby {

    private final LobbyId id;
    private final String name;
    private final PlayerId createdBy;

    private Lobby(LobbyId id, String name, PlayerId createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
    }

    public static Lobby create(String name, PlayerId createdBy) {
        return new Lobby(LobbyId.generate(), name, createdBy);
    }

    public LobbyId id() {
        return id;
    }

    public Snapshot snapshot() {
        return new Snapshot(id, name, createdBy);
    }

    public record Snapshot(LobbyId id, String name, PlayerId createdBy) {}

}
