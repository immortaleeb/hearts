package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.lobby.shared.GameId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record GameStarted(LobbyId lobby, GameId game) implements LobbyEvent {
}
