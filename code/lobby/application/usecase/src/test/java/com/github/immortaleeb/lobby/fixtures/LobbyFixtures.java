package com.github.immortaleeb.lobby.fixtures;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.builders.LobbySnapshotBuilder;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.List;

public class LobbyFixtures {

    public static final LobbyId LOBBY_ID = LobbyId.generate();
    public static final PlayerId PLAYER_1 = PlayerId.generate();

    public static LobbySnapshotBuilder existingLobby() {
        return new LobbySnapshotBuilder()
                .withId(LOBBY_ID)
                .withName("Existing lobby")
                .withCreatedBy(PLAYER_1)
                .withPlayers(List.of(PLAYER_1));
    }

}
