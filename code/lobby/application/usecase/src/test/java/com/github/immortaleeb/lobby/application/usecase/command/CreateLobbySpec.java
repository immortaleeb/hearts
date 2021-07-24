package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.LobbyCreated;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class CreateLobbySpec extends LobbySpec {

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

        assertThat(lobbyRepository.lastRaisedEvent(), is(instanceOf(LobbyCreated.class)));
        LobbyCreated lobbyCreated = (LobbyCreated) lobbyRepository.lastRaisedEvent();

        assertThat(lobbyCreated.lobby(), is(equalTo(savedLobby.id())));
        assertThat(lobbyCreated.name(), is(equalTo("Example lobby")));
        assertThat(lobbyCreated.createdBy(), is(equalTo(playerId)));
    }

}