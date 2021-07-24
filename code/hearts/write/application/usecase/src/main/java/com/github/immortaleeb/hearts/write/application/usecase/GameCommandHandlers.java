package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hearts.write.domain.GameRepository;

public class GameCommandHandlers {

    public static void registerAll(CommandHandlerRegistry commandHandlerRegistry, GameRepository gameRepository) {
        commandHandlerRegistry.register(StartGame.class, new StartGameHandler(gameRepository));
        commandHandlerRegistry.register(PassCards.class, new PassCardsHandler(gameRepository));
        commandHandlerRegistry.register(PlayCard.class, new PlayCardHandler(gameRepository));
    }

}