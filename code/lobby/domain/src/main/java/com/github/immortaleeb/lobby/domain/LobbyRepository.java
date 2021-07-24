package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.Optional;

public interface LobbyRepository {

    Optional<Lobby> find(LobbyId lobbyId);

    void save(Lobby lobby);
}
