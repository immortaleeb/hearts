package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.domain.PlayerJoinedLobby;
import com.github.immortaleeb.lobby.shared.LobbyFull;
import com.github.immortaleeb.lobby.shared.PlayerAlreadyJoinedLobby;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.lobby.domain.LobbyState.READY_TO_PLAY;
import static com.github.immortaleeb.lobby.domain.LobbyState.WAITING_FOR_PLAYERS;
import static com.github.immortaleeb.lobby.fixtures.LobbyFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JoinLobbySpec extends LobbySpec {

    private static final PlayerId PLAYER_5 = PlayerId.generate();

    @Test
    void lets_new_player_join_the_lobby() {
        PlayerId[] players1 = new PlayerId[]{PLAYER_1};
        lobbyRepository.givenExisting(existingLobby()
                .withCreatedBy(players1[0])
                .withPlayers(List.of(players1))
                .build());

        joinLobby(PLAYER_2);

        PlayerId[] players = new PlayerId[]{PLAYER_1, PLAYER_2};
        assertThat(lobbyRepository.lastSaved(), is(equalTo(existingLobby()
                .withCreatedBy(players[0])
                .withPlayers(List.of(players))
                .build())));
        assertThat(lobbyRepository.raisedEvents(), is(equalTo(List.of(
            new PlayerJoinedLobby(LOBBY_ID, PLAYER_2)
        ))));
    }

    @Test
    void throws_exception_when_player_has_already_joined_the_lobby() {
        lobbyRepository.givenExisting(existingLobby()
                .withPlayers(List.of(PLAYER_1, PLAYER_2))
                .build());

        assertThrows(PlayerAlreadyJoinedLobby.class, () -> joinLobby(PLAYER_2));
    }

    @Test
    void throws_exception_when_player_tries_to_join_full_lobby() {
        lobbyRepository.givenExisting(existingLobby()
                .withState(READY_TO_PLAY)
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4))
                .build());

        assertThrows(LobbyFull.class, () -> joinLobby(PLAYER_5));
    }

    @Test
    void lobby_changes_to_ready_to_play_when_4th_player_joins() {
        lobbyRepository.givenExisting(existingLobby()
                .withState(WAITING_FOR_PLAYERS)
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3))
                .build());

        joinLobby(PLAYER_4);

        assertThat(lobbyRepository.lastSaved(), is(equalTo(existingLobby()
                .withState(READY_TO_PLAY)
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4))
                .build())));
    }

    private void joinLobby(PlayerId player) {
        dispatcher.dispatch(new JoinLobby(LOBBY_ID, player));
    }

}
