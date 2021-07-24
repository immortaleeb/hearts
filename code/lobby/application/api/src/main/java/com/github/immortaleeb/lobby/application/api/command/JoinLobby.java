package com.github.immortaleeb.lobby.application.api.command;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record JoinLobby(LobbyId lobby, PlayerId player) implements Command {
}
