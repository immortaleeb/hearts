package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.lobby.fakes.FakeGameStarter;
import com.github.immortaleeb.lobby.fakes.FakeLobbyRepository;
import com.github.immortaleeb.lobby.shared.GameId;
import org.junit.jupiter.api.BeforeEach;

abstract class LobbySpec {

    protected static final GameId GAME_ID = GameId.of("60e1c43c-c252-4642-ab23-5d9604b441e4");

    protected CommandDispatcher dispatcher;
    protected FakeLobbyRepository lobbyRepository;
    protected FakeGameStarter gameStarter;

    @BeforeEach
    void setUp() {
        lobbyRepository = new FakeLobbyRepository();
        gameStarter = new FakeGameStarter(GAME_ID);

        CommandHandlerRegistry registry = new CommandHandlerRegistry();
        LobbyCommandHandlers.registerAll(registry, lobbyRepository, gameStarter);

        dispatcher = new CommandHandlerDispatcher(registry);
    }

}
