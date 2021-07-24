package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;

public record LobbyCreated(LobbyId lobby, String name, PlayerId createdBy) implements LobbyEvent {
}
