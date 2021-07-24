package com.github.immortaleeb.lobby.application.api.query;

import com.github.immortaleeb.common.application.api.Query;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.List;

public interface ListLobbies extends Query {

    List<LobbySummary> listLobbies();

    record LobbySummary(LobbyId id, String name, PlayerId createdBy) { }

}
