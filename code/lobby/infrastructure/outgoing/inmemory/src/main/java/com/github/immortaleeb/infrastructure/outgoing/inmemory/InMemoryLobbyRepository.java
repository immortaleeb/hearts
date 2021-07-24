package com.github.immortaleeb.infrastructure.outgoing.inmemory;

import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryLobbyRepository implements LobbyRepository {

    private final Map<LobbyId, Lobby.Snapshot> lobbyById = new HashMap<>();

    @Override
    public List<Lobby> findAll() {
        return lobbyById.values()
                .stream()
                .map(Lobby::restoreFrom)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lobby> find(LobbyId lobbyId) {
        return Optional.ofNullable(lobbyById.get(lobbyId))
                .map(Lobby::restoreFrom);
    }

    @Override
    public void save(Lobby lobby) {
        lobbyById.put(lobby.id(), lobby.snapshot());
    }

}
