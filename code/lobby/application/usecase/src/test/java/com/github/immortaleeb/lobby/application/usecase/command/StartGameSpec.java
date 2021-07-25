package com.github.immortaleeb.lobby.application.usecase.command;

import com.github.immortaleeb.lobby.application.api.command.StartGame;
import com.github.immortaleeb.lobby.domain.GameStarted;
import com.github.immortaleeb.lobby.domain.LobbyState;
import com.github.immortaleeb.lobby.fakes.FakeGameStarter;
import com.github.immortaleeb.lobby.shared.LobbyNotFound;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.lobby.domain.LobbyState.PLAYING;
import static com.github.immortaleeb.lobby.fixtures.LobbyFixtures.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StartGameSpec extends LobbySpec {

    @Test
    void calls_start_game_on_external_game_starter() {
        lobbyRepository.givenExisting(existingLobby()
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4))
                .build());

        dispatcher.dispatch(new StartGame(LOBBY_ID));

        assertThat(gameStarter.lastStartedGame(), is(equalTo(
                new FakeGameStarter.StartedGame(GAME_ID, List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4)))));
    }

    @Test
    void persists_started_game_on_lobby() {
        lobbyRepository.givenExisting(existingLobby()
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4))
                .build());

        dispatcher.dispatch(new StartGame(LOBBY_ID));

        assertThat(lobbyRepository.lastSaved(), is(equalTo(existingLobby()
                .withPlayers(List.of(PLAYER_1, PLAYER_2, PLAYER_3, PLAYER_4))
                .withGame(GAME_ID)
                .withState(PLAYING)
                .build())));
        assertThat(lobbyRepository.lastRaisedEvent(), is(equalTo(new GameStarted(LOBBY_ID, GAME_ID))));
    }

    @Test
    void throws_exception_when_lobby_does_not_exist() {
        assertThrows(LobbyNotFound.class, () -> dispatcher.dispatch(new StartGame(LOBBY_ID)));
    }
}
