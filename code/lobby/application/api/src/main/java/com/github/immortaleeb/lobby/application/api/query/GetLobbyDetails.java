package com.github.immortaleeb.lobby.application.api.query;

import com.github.immortaleeb.common.application.api.Query;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyGameId;
import com.github.immortaleeb.lobby.shared.LobbyId;
import com.github.immortaleeb.lobby.shared.LobbyState;

import java.util.List;

public interface GetLobbyDetails extends Query {

    LobbyDetails getDetails(LobbyId lobbyId);

    record LobbyDetails(LobbyId id, LobbyState state, String name, PlayerId createdBy, List<PlayerId> players, LobbyGameId game) { }

}
