package com.github.immortaleeb.lobby.application.api.command;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.common.shared.PlayerId;

public record CreateLobby(String name, PlayerId createdBy) implements Command {
}
