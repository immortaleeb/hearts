package com.github.immortaleeb.lobby.application.usecase.query;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.application.api.query.ListLobbies;
import com.github.immortaleeb.lobby.domain.Lobby;
import com.github.immortaleeb.lobby.fakes.FakeLobbyRepository;
import com.github.immortaleeb.lobby.shared.LobbyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ListLobbiesHandlerTest {

    private FakeLobbyRepository repository;
    private ListLobbies listLobbies;

    @BeforeEach
    void setUp() {
        repository = new FakeLobbyRepository();
        listLobbies = new ListLobbiesHandler(repository);
    }

    @Test
    void list_lobbies_returns_all_stored_lobbies() {
        // given
        LobbyId lobbyId1 = LobbyId.of("9805f468-cac6-46c1-a1bc-bd4694efc4b7");
        LobbyId lobbyId2 = LobbyId.of("180ea7f9-8213-42ba-b620-6fba395dc1e8");
        LobbyId lobbyId3 = LobbyId.of("8d429618-f5d3-4899-b6c2-5e21493ba3ff");

        PlayerId player1 = PlayerId.of("a99a4739-179d-44dc-bace-057a18baa7b8");
        PlayerId player2 = PlayerId.of("572838bf-2c62-4a4f-bfaf-2e11d0c22f08");
        PlayerId player3 = PlayerId.of("e76d3e73-593d-4df6-9641-1674642dae6f");

        repository.givenExisting(new Lobby.Snapshot(lobbyId1, "Lobby 1", player1, List.of(player1), null));
        repository.givenExisting(new Lobby.Snapshot(lobbyId2, "Lobby 2", player2, List.of(player2), null));
        repository.givenExisting(new Lobby.Snapshot(lobbyId3, "Lobby 3", player3, List.of(player3), null));

        // when
        List<ListLobbies.LobbySummary> lobbies = listLobbies.listLobbies();

        // then
        assertThat(lobbies, is(equalTo(List.of(
                new ListLobbies.LobbySummary(lobbyId1, "Lobby 1", player1),
                new ListLobbies.LobbySummary(lobbyId2, "Lobby 2", player2),
                new ListLobbies.LobbySummary(lobbyId3, "Lobby 3", player3)
        ))));
    }
}
