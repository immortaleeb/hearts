package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;

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
