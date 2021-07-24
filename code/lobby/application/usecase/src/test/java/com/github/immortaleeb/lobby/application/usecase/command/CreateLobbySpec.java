package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.domain.Lobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class CreateLobbySpec {

    private CommandDispatcher dispatcher;
    private FakeLobbyRepository lobbyRepository;

    @BeforeEach
    void setUp() {
        lobbyRepository = new FakeLobbyRepository();

        CommandHandlerRegistry registry = new CommandHandlerRegistry();
        registry.register(CreateLobby.class, new CreateLobbyCommandHandler(lobbyRepository));

        dispatcher = new CommandHandlerDispatcher(registry);
    }

    @Test
    void creates_new_lobby() {
        // given
        PlayerId playerId = PlayerId.generate();

        // when
        dispatcher.dispatch(new CreateLobby("Example lobby", playerId));

        // then
        Lobby.Snapshot savedLobby = lobbyRepository.lastSaved();
        assertThat(savedLobby.id(), is(notNullValue()));
        assertThat(savedLobby.name(), is(equalTo("Example lobby")));
        assertThat(savedLobby.createdBy(), is(equalTo(playerId)));
    }

}