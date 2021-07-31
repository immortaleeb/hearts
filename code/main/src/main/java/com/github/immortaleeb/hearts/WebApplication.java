package com.github.immortaleeb.hearts;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.hearts.common.infrastructure.projection.inmemory.InMemoryGameSummaryStore;
import com.github.immortaleeb.hearts.common.projection.api.GameSummaryStore;
import com.github.immortaleeb.hearts.read.infrastructure.rest.GameSummaryProjectionReadRepository;
import com.github.immortaleeb.hearts.write.application.usecase.GameCommandHandlers;
import com.github.immortaleeb.hearts.write.domain.GameRepository;
import com.github.immortaleeb.hearts.write.domain.GameStarted;
import com.github.immortaleeb.hearts.write.domain.GameSummaryWriteRepository;
import com.github.immortaleeb.hearts.write.infrastructure.eventsourcing.EventSourcedGameRepository;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.api.EventStore;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.EventDispatcher;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.EventListenerRegistry;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.InMemoryEventStore;
import com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection.GameProjectionsHandler;
import com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection.GameSummaryProjectionWriteRepository;
import com.github.immortaleeb.infrastructure.outgoing.inmemory.InMemoryLobbyRepository;
import com.github.immortaleeb.lobby.application.api.query.GetLobbyDetails;
import com.github.immortaleeb.lobby.application.api.query.ListLobbies;
import com.github.immortaleeb.lobby.application.usecase.command.LobbyCommandHandlers;
import com.github.immortaleeb.lobby.application.usecase.query.GetLobbyDetailsHandler;
import com.github.immortaleeb.lobby.application.usecase.query.ListLobbiesHandler;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.domain.LobbyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.github.immortaleeb.hearts", "com.github.immortaleeb.lobby"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }

    @Bean
    public EventListenerRegistry eventListenerRegistry() {
        return new EventListenerRegistry();
    }

    @Bean
    public EventDispatcher eventDispatcher(EventListenerRegistry eventListenerRegistry) {
        return new EventDispatcher(eventListenerRegistry);
    }

    @Bean
    public EventStore eventStore(EventDispatcher eventDispatcher) {
        return new InMemoryEventStore(eventDispatcher);
    }

    @Bean
    public GameRepository gameRepository(EventStore eventStore) {
        return new EventSourcedGameRepository(eventStore);
    }

    @Bean
    public LobbyRepository lobbyRepository() {
        return new InMemoryLobbyRepository();
    }

    public GameStarter gameStarter(CommandDispatcher commandDispatcher) {
        return new GameDomainGameStarter(commandDispatcher);
    }

    @Bean
    public CommandHandlerRegistry commandHandlerRegistry() {
        return new CommandHandlerRegistry();
    }

    @Bean
    public GameSummaryStore gameSummaryStore() {
        return new InMemoryGameSummaryStore();
    }

    @Bean
    public GameSummaryProjectionReadRepository gameSummaryReadRepository(GameSummaryStore gameSummaryStore) {
        return new GameSummaryProjectionReadRepository(gameSummaryStore);
    }

    @Bean
    public GameSummaryWriteRepository gameSummaryWriteRepository(GameSummaryStore gameSummaryStore) {
        return new GameSummaryProjectionWriteRepository(gameSummaryStore);
    }

    @Bean
    public CommandDispatcher commandDispatcher(CommandHandlerRegistry commandHandlerRegistry, LobbyRepository lobbyRepository, GameRepository gameRepository,
     GameSummaryWriteRepository gameSummaryWriteRepository, EventListenerRegistry eventListenerRegistry) {
        CommandHandlerDispatcher dispatcher = new CommandHandlerDispatcher(commandHandlerRegistry);

        initializeEventListeners(eventListenerRegistry, dispatcher);
        initializeCommandHandlers(commandHandlerRegistry, lobbyRepository, gameRepository, gameStarter(dispatcher), gameSummaryWriteRepository);

        return dispatcher;
    }

    private void initializeCommandHandlers(CommandHandlerRegistry commandHandlerRegistry, LobbyRepository lobbyRepository, GameRepository gameRepository,
        GameStarter gameStarter, GameSummaryWriteRepository gameSummaryWriteRepository) {
        LobbyCommandHandlers.registerAll(commandHandlerRegistry, lobbyRepository, gameStarter);
        GameCommandHandlers.registerAll(commandHandlerRegistry, gameRepository, gameSummaryWriteRepository);
    }

    private void initializeEventListeners(EventListenerRegistry eventListenerRegistry, CommandHandlerDispatcher dispatcher) {
        GameProjectionsHandler gameProjectionsHandler = new GameProjectionsHandler(dispatcher);
        eventListenerRegistry.register(GameStarted.class, gameProjectionsHandler::process);
    }

    @Bean
    public ListLobbies listLobbies(LobbyRepository lobbyRepository) {
        return new ListLobbiesHandler(lobbyRepository);
    }

    @Bean
    public GetLobbyDetails lobbyDetails(LobbyRepository lobbyRepository) {
        return new GetLobbyDetailsHandler(lobbyRepository);
    }

}
