package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.NoResultCommandHandler;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;

class PassCardsHandler implements NoResultCommandHandler<PassCards> {

    private final GameRepository gameRepository;

    public PassCardsHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void handleNoResult(PassCards passCards) {
        Game game = gameRepository.load(passCards.gameId());
        game.passCards(passCards.fromPlayer(), passCards.cards());
        gameRepository.save(game);
    }

}
