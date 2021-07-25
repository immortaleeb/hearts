package com.github.immortaleeb.lobby.fakes;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.shared.LobbyGameId;

import java.util.ArrayList;
import java.util.List;

public class FakeGameStarter implements GameStarter {

    private final LobbyGameId gameId;
    private final List<StartedGame> startedGames = new ArrayList<>();

    public FakeGameStarter(LobbyGameId gameId) {
        this.gameId = gameId;
    }

    @Override
    public LobbyGameId startGame(List<PlayerId> players) {
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

    public static record StartedGame(LobbyGameId gameId, List<PlayerId> players) { }
}
