package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.Game;
import com.github.immortaleeb.hearts.domain.GameRepository;

class PassCardsHandler {

    private final GameRepository gameRepository;

    public PassCardsHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void handle(PassCards passCards) {
        Game game = gameRepository.load(passCards.gameId());
        game.passCards(passCards.fromPlayer(), passCards.cards());
        gameRepository.save(game);
    }
}
