package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.LobbyGameId;

import java.util.List;

public interface GameStarter {

    LobbyGameId startGame(List<PlayerId> players);

}
