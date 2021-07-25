package com.github.immortaleeb.lobby.builders;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.shared.LobbyState;
import com.github.immortaleeb.lobby.shared.LobbyGameId;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.List;

public class LobbySnapshotBuilder {

    private LobbyId id;
    private LobbyState state;
    private String name;
    private PlayerId createdBy;
    private List<PlayerId> players;
    private LobbyGameId game;

    public LobbySnapshotBuilder withId(LobbyId id) {
        this.id = id;
        return this;
    }

    public LobbySnapshotBuilder withState(LobbyState state) {
        this.state = state;
        return this;
    }

    public LobbySnapshotBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LobbySnapshotBuilder withCreatedBy(PlayerId createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LobbySnapshotBuilder withPlayers(List<PlayerId> players) {
        this.players = players;
        return this;
    }

    public LobbySnapshotBuilder withGame(LobbyGameId game) {
        this.game = game;
        return this;
    }

    public Lobby.Snapshot build() {
        return new Lobby.Snapshot(id, state, name, createdBy, players, game);
    }

}
