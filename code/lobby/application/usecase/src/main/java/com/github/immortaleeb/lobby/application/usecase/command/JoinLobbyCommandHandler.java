package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.NoResultCommandHandler;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;

import java.util.Optional;

public class JoinLobbyCommandHandler implements NoResultCommandHandler<JoinLobby> {

    private final LobbyRepository lobbyRepository;

    public JoinLobbyCommandHandler(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public void handleNoResult(JoinLobby command) {
        Optional<Lobby> existingLobby = lobbyRepository.find(command.lobby());

        Lobby lobby = existingLobby.get();
        lobby.join(command.player());

        lobbyRepository.save(lobby);
    }

}
