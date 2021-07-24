package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandHandler;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;

class CreateLobbyCommandHandler implements CommandHandler<LobbyId, CreateLobby> {

    private final LobbyRepository lobbyRepository;

    CreateLobbyCommandHandler(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public LobbyId handle(CreateLobby command) {
        Lobby lobby = Lobby.create(command.name(), command.createdBy());
        lobbyRepository.save(lobby);

        return lobby.id();
    }

}
