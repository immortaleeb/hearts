package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.shared.GameId;

class StartGameHandler {

    private final GameRepository gameRepository;

    public StartGameHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameId handle(StartGame startGame) {
        Game game = Game.startWith(startGame.players());
        gameRepository.save(game);

        return game.id();
    }

}
