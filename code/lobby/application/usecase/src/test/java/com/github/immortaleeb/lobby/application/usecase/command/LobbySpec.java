package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.lobby.repository.FakeLobbyRepository;
import org.junit.jupiter.api.BeforeEach;

abstract class LobbySpec {

    protected CommandDispatcher dispatcher;
    protected FakeLobbyRepository lobbyRepository;

    @BeforeEach
    void setUp() {
        lobbyRepository = new FakeLobbyRepository();

        CommandHandlerRegistry registry = new CommandHandlerRegistry();
        LobbyCommandHandlers.registerAll(registry, lobbyRepository);

        dispatcher = new CommandHandlerDispatcher(registry);
    }

}