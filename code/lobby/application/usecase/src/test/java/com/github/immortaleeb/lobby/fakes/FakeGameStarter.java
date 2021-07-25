package com.github.immortaleeb.lobby.fakes;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.shared.GameId;

import java.util.ArrayList;
import java.util.List;

public class FakeGameStarter implements GameStarter {

    private final GameId gameId;
    private final List<StartedGame> startedGames = new ArrayList<>();

    public FakeGameStarter(GameId gameId) {
        this.gameId = gameId;
    }

    @Override
    public GameId startGame(List<PlayerId> players) {
        StartedGame startedGame = new StartedGame(gameId, new ArrayList<>(players));
        startedGames.add(startedGame);
        return startedGame.gameId();
    }

    public StartedGame lastStartedGame() {
        if (startedGames.isEmpty()) {
            throw new IllegalStateException("At least one game should be started");
        }

        return startedGames.get(startedGames.size() - 1);
    }

    public static record StartedGame(GameId gameId, List<PlayerId> players) { }
}
