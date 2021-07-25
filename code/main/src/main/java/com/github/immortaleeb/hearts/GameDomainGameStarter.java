package com.github.immortaleeb.hearts;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.shared.GameId;

import java.util.List;

public class GameDomainGameStarter implements GameStarter {
    private final CommandDispatcher commandDispatcher;

    public GameDomainGameStarter(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public GameId startGame(List<PlayerId> players) {
        com.github.immortaleeb.hearts.write.shared.GameId gameId = commandDispatcher.dispatch(new StartGame(players));
        return GameId.of(gameId.asString());
    }

}
