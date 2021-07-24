package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record PlayerJoinedLobby(LobbyId lobby, PlayerId player) implements LobbyEvent {
}
