package com.github.immortaleeb.hearts;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.infrastructure.outgoing.inmemory.InMemoryLobbyRepository;
import com.github.immortaleeb.lobby.application.api.query.GetLobbyDetails;
import com.github.immortaleeb.lobby.application.api.query.ListLobbies;
import com.github.immortaleeb.lobby.application.usecase.command.LobbyCommandHandlers;
import com.github.immortaleeb.lobby.application.usecase.query.GetLobbyDetailsHandler;
import com.github.immortaleeb.lobby.application.usecase.query.ListLobbiesHandler;
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
    public LobbyRepository lobbyRepository() {
        return new InMemoryLobbyRepository();
    }

    @Bean
    public CommandHandlerRegistry commandHandlerRegistry(LobbyRepository lobbyRepository) {
        CommandHandlerRegistry commandHandlerRegistry = new CommandHandlerRegistry();

        LobbyCommandHandlers.registerAll(commandHandlerRegistry, lobbyRepository);

        return commandHandlerRegistry;
    }

    @Bean
    public CommandDispatcher commandDispatcher(CommandHandlerRegistry commandHandlerRegistry) {
        return new CommandHandlerDispatcher(commandHandlerRegistry);
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
