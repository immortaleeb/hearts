package com.github.immortaleeb.lobby.application.usecase.query;

import com.github.immortaleeb.lobby.application.api.query.GetLobbyDetails;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;
import com.github.immortaleeb.lobby.shared.LobbyNotFound;

public class GetLobbyDetailsHandler implements GetLobbyDetails {

    private final LobbyRepository lobbyRepository;

    public GetLobbyDetailsHandler(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public LobbyDetails getDetails(LobbyId lobbyId) {
        return lobbyRepository.find(lobbyId)
                .map(Lobby::snapshot)
                .map(snapshot -> new LobbyDetails(snapshot.id(), snapshot.state(), snapshot.name(), snapshot.createdBy(), snapshot.players(), snapshot.game()))
                .orElseThrow(() -> new LobbyNotFound("Could not find lobby with id " + lobbyId.asString()));
    }
}
