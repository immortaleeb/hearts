package com.github.immortaleeb.hearts.write.application.usecase;

import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hearts.write.application.api.ProjectGameEvent;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;
import com.github.immortaleeb.hearts.write.domain.PlayerHandWriteRepository;

public class GameCommandHandlers {

    public static void registerAll(CommandHandlerRegistry commandHandlerRegistry, GameRepository gameRepository,
        GameSummaryWriteRepository gameSummaryWriteRepository, PlayerHandWriteRepository playerHandWriteRepository) {
        commandHandlerRegistry.register(StartGame.class, new StartGameHandler(gameRepository));
        commandHandlerRegistry.register(PassCards.class, new PassCardsHandler(gameRepository));
        commandHandlerRegistry.register(PlayCard.class, new PlayCardHandler(gameRepository));
        commandHandlerRegistry.register(ProjectGameEvent.class, new ProjectGameEventHandler(gameSummaryWriteRepository, playerHandWriteRepository));
    }

}
