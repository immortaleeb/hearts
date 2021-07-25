package com.github.immortaleeb.lobby.application.api.command;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record StartGame(LobbyId lobbyId) implements Command {
}
