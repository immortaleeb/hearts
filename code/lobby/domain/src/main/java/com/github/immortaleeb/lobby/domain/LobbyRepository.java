package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.List;
import java.util.Optional;

public interface LobbyRepository {

    List<Lobby> findAll();

    Optional<Lobby> find(LobbyId lobbyId);

    void save(Lobby lobby);
}
