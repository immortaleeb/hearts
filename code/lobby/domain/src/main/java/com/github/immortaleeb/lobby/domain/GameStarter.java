package com.github.immortaleeb.lobby.domain;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.shared.GameId;

import java.util.List;

public interface GameStarter {

    GameId startGame(List<PlayerId> players);

}
