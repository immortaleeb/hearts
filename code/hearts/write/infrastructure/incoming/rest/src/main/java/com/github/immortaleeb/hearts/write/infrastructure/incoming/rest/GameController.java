package com.github.immortaleeb.hearts.write.infrastructure.incoming.rest;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hears.common.shared.GameId;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {

    private final CommandDispatcher commandDispatcher;

    public GameController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping("/api/v1/games/start")
    public StartGameResponse startGame(@RequestBody StartGameRequest request) {
        GameId gameId = commandDispatcher.dispatch(new StartGame(request.players));
        return new StartGameResponse(gameId);
    }

    record StartGameRequest(List<PlayerId> players) { }
    record StartGameResponse(GameId id) { }
}
