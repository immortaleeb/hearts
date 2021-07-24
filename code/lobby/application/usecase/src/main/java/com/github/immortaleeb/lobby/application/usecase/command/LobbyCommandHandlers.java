package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.domain.LobbyRepository;

public class LobbyCommandHandlers {

    public static void registerAll(CommandHandlerRegistry commandHandlerRegistry, LobbyRepository lobbyRepository) {
        commandHandlerRegistry.register(CreateLobby.class, new CreateLobbyCommandHandler(lobbyRepository));
        commandHandlerRegistry.register(JoinLobby.class, new JoinLobbyCommandHandler(lobbyRepository));
    }

}
