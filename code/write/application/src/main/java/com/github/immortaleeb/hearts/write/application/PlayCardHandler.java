package com.github.immortaleeb.hearts.write.application;

import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;

class PlayCardHandler {

    private final GameRepository gameRepository;

    public PlayCardHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    void handle(PlayCard command) {
        Game game = gameRepository.load(command.gameId());
        game.playCard(command.player(), command.card());
        gameRepository.save(game);
    }

}
