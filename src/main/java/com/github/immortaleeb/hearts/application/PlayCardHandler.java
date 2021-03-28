package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameRepository;

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
