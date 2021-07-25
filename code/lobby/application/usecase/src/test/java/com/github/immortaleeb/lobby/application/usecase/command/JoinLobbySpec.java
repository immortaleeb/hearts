package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.domain.PlayerJoinedLobby;
import com.github.immortaleeb.lobby.shared.LobbyFull;
import com.github.immortaleeb.lobby.shared.PlayerAlreadyJoinedLobby;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.lobby.fixtures.LobbyFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JoinLobbySpec extends LobbySpec {

    private static final PlayerId PLAYER_5 = PlayerId.generate();

    @Test
    void lets_new_player_join_the_lobby() {
        lobbyRepository.givenExisting(lobbyWithPlayers(PLAYER_1));

        joinLobby(PLAYER_2);

        assertThat(lobbyRepository.lastSaved(), is(equalTo(lobbyWithPlayers(PLAYER_1, PLAYER_2))));
        assertThat(lobbyRepository.raisedEvents(), is(equalTo(List.of(
            new PlayerJoinedLobby(LOBBY_ID, PLAYER_2)
        ))));
    }

    @Test
    void throws_exception_when_player_has_already_joined_the_lobby() {
        lobbyRepository.givenExisting(lobbyWithPlayers(PLAYER_1, PLAYER_2));

        assertThrows(PlayerAlreadyJoinedLobby.class, () -> joinLobby(PLAYER_2));
    }

    @Test
    void throws_exception_when_player_tries_to_join_full_lobby() {
        lobbyRepository.givenExisting(lobbyWithPlayers(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4));

        assertThrows(LobbyFull.class, () -> joinLobby(PLAYER_5));
    }

    private void joinLobby(PlayerId player) {
        dispatcher.dispatch(new JoinLobby(LOBBY_ID, player));
    }

    private static Lobby.Snapshot lobbyWithPlayers(PlayerId... players) {
        return existingLobby()
                .withCreatedBy(players[0])
                .withPlayers(List.of(players))
                .build();
    }

}
