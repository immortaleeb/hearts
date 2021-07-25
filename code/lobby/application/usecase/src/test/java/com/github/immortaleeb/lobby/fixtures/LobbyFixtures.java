package com.github.immortaleeb.lobby.fixtures;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.builders.LobbySnapshotBuilder;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.List;

import static com.github.immortaleeb.lobby.domain.LobbyState.WAITING_FOR_PLAYERS;

public class LobbyFixtures {

    public static final LobbyId LOBBY_ID = LobbyId.generate();
    public static final PlayerId PLAYER_1 = PlayerId.generate();
    public static final PlayerId PLAYER_2 = PlayerId.generate();
    public static final PlayerId PLAYER_3 = PlayerId.generate();
    public static final PlayerId PLAYER_4 = PlayerId.generate();

    public static LobbySnapshotBuilder existingLobby() {
        return new LobbySnapshotBuilder()
                .withId(LOBBY_ID)
                .withState(WAITING_FOR_PLAYERS)
                .withName("Existing lobby")
                .withCreatedBy(PLAYER_1)
                .withPlayers(List.of(PLAYER_1));
    }

}
