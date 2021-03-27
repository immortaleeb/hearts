package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.domain.GameRepository;
import com.github.immortaleeb.hearts.shared.GameId;

public class CommandDispatcher {

    private final StartGameHandler startGameHandler;
    private final PassCardsHandler passCardsHandler;

    public CommandDispatcher(GameRepository gameRepository) {
        this.startGameHandler = new StartGameHandler(gameRepository);
        this.passCardsHandler = new PassCardsHandler(gameRepository);
    }

    public GameId dispatch(StartGame startGame) {
        return this.startGameHandler.handle(startGame);
    }

    public void dispatch(PassCards passCards) {
        passCardsHandler.handle(passCards);
    }

}

