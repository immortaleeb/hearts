package com.github.immortaleeb.lobby.application.usecase.query;

import com.github.immortaleeb.lobby.application.api.query.ListLobbies;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ListLobbiesHandler implements ListLobbies {

    private final LobbyRepository lobbyRepository;

    public ListLobbiesHandler(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public List<LobbySummary> listLobbies() {
        return lobbyRepository.findAll()
                .stream()
                .map(Lobby::snapshot)
                .map(snapshot -> new LobbySummary(snapshot.id(), snapshot.name(), snapshot.createdBy()))
                .collect(Collectors.toList());
    }

}
