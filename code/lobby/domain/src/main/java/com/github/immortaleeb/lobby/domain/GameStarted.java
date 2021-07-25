package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.lobby.shared.LobbyGameId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record GameStarted(LobbyId lobby, LobbyGameId game) implements LobbyEvent {
}
