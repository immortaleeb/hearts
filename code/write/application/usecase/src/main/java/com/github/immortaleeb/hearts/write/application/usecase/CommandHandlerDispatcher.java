package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.hearts.write.application.api.*;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.shared.GameId;

public class CommandHandlerDispatcher implements CommandDispatcher {

    private final StartGameHandler startGameHandler;
    private final PassCardsHandler passCardsHandler;
    private final PlayCardHandler playCardHandler;

    public CommandHandlerDispatcher(GameRepository gameRepository) {
        this.startGameHandler = new StartGameHandler(gameRepository);
        this.passCardsHandler = new PassCardsHandler(gameRepository);
        this.playCardHandler = new PlayCardHandler(gameRepository);
    }

    public void dispatch(Command command) {
        if (command instanceof StartGame startGame) {
            dispatch(startGame);
        } else if (command instanceof PassCards passCards) {
            dispatch(passCards);
        } else if (command instanceof PlayCard playCard) {
            dispatch(playCard);
        } else {
            throw new RuntimeException("Unknown command of type " + command.getClass());
        }
    }

    @Override
    public GameId dispatch(StartGame startGame) {
        return this.startGameHandler.handle(startGame);
    }

    @Override
    public void dispatch(PassCards passCards) {
        passCardsHandler.handle(passCards);
    }

    @Override
    public void dispatch(PlayCard playCard) {
        playCardHandler.handle(playCard);
    }

}

