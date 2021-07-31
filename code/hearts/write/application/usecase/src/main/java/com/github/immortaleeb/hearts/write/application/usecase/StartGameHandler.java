package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.CommandHandler;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hears.common.shared.GameId;

class StartGameHandler implements CommandHandler<GameId, StartGame> {

    private final GameRepository gameRepository;

    public StartGameHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameId handle(StartGame startGame) {
        Game game = Game.startWith(startGame.players());
        gameRepository.save(game);

        return game.id();
    }

}
