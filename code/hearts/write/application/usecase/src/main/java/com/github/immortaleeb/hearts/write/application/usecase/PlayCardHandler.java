package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.NoResultCommandHandler;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.domain.Game;
import com.github.immortaleeb.hearts.write.domain.GameRepository;

class PlayCardHandler implements NoResultCommandHandler<PlayCard> {

    private final GameRepository gameRepository;

    public PlayCardHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void handleNoResult(PlayCard command) {
        Game game = gameRepository.load(command.gameId());
        game.playCard(command.player(), command.card());
        gameRepository.save(game);
    }

}
