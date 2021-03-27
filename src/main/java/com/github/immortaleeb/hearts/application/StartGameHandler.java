package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameRepository;
import com.github.immortaleeb.hearts.shared.GameId;

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
