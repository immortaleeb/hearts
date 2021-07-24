package com.github.immortaleeb.lobby.application.usecase.query;

import com.github.immortaleeb.lobby.application.api.query.GetLobbyDetails;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.repository.FakeLobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;
import com.github.immortaleeb.lobby.shared.LobbyNotFound;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.immortaleeb.lobby.fixtures.LobbyFixtures.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetLobbyDetailsHandlerTest {

    private FakeLobbyRepository repository;
    private GetLobbyDetails getLobbyDetails;

    @BeforeEach
    void setUp() {
        repository = new FakeLobbyRepository();
        getLobbyDetails = new GetLobbyDetailsHandler(repository);
    }

    @Test
    void get_lobby_details_throws_exception_when_lobby_cannot_be_found() {
        LobbyId unknownId = LobbyId.of("49a850a2-308e-4e93-b1a9-144e5689d511");

        assertThrows(LobbyNotFound.class, () -> getLobbyDetails.getDetails(unknownId));
    }

    @Test
    void get_lobby_details_gets_lobby_details() {
        Lobby.Snapshot existingLobby = existingLobby().build();
        repository.givenExisting(existingLobby);

        GetLobbyDetails.LobbyDetails lobbyDetails = getLobbyDetails.getDetails(existingLobby.id());

        MatcherAssert.assertThat(lobbyDetails, CoreMatchers.is(CoreMatchers.equalTo(new GetLobbyDetails.LobbyDetails(
                LOBBY_ID, "Existing lobby", PLAYER_1, List.of(PLAYER_1)
        ))));
    }
}