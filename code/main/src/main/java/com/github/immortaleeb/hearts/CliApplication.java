package com.github.immortaleeb.hearts;

import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.application.usecase.GameCommandHandlers;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.infrastructure.eventsourcing.EventSourcedGameRepository;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.api.EventStore;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.EventDispatcher;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.EventListenerRegistry;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.inmemory.InMemoryEventStore;
import com.github.immortaleeb.hearts.write.infrastructure.incoming.cli.PlayerController;
import com.github.immortaleeb.hearts.write.infrastructure.incoming.cli.PlayerInputHandler;
import com.github.immortaleeb.hearts.write.infrastructure.incoming.cli.SimplePlayerInputHandler;
import com.github.immortaleeb.hearts.write.infrastructure.incoming.cli.StdinPlayerInputHandler;
import com.github.immortaleeb.infrastructure.outgoing.inmemory.InMemoryLobbyRepository;
import com.github.immortaleeb.lobby.application.api.command.CreateLobby;
import com.github.immortaleeb.lobby.application.api.command.JoinLobby;
import com.github.immortaleeb.lobby.application.api.command.StartGame;
import com.github.immortaleeb.lobby.application.usecase.command.LobbyCommandHandlers;
import com.github.immortaleeb.lobby.domain.GameStarter;
import com.github.immortaleeb.lobby.shared.LobbyId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CliApplication {

    public static void main(String[] args) {
        EventListenerRegistry eventListenerRegistry = new EventListenerRegistry();
        EventDispatcher eventDispatcher = new EventDispatcher(eventListenerRegistry);

        CommandHandlerRegistry commandHandlerRegistry = new CommandHandlerRegistry();
        CommandHandlerDispatcher commandDispatcher = new CommandHandlerDispatcher(commandHandlerRegistry);

        EventStore eventStore = new InMemoryEventStore(eventDispatcher);

        InMemoryLobbyRepository lobbyRepository = new InMemoryLobbyRepository();
        GameStarter gameStarter = new GameDomainGameStarter(commandDispatcher);
        LobbyCommandHandlers.registerAll(commandHandlerRegistry, lobbyRepository, gameStarter);

        GameRepository gameRepository = new EventSourcedGameRepository(eventStore);
        GameCommandHandlers.registerAll(commandHandlerRegistry, gameRepository);

        List<PlayerId> players = Arrays.asList(
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate());

        LobbyId lobbyId = commandDispatcher.dispatch(new CreateLobby("My Lobby", players.get(0)));
        commandDispatcher.dispatch(new JoinLobby(lobbyId, players.get(1)));
        commandDispatcher.dispatch(new JoinLobby(lobbyId, players.get(2)));
        commandDispatcher.dispatch(new JoinLobby(lobbyId, players.get(3)));

        List<PlayerInputHandler> playerInputHandlers = List.of(
                new StdinPlayerInputHandler(),
                new SimplePlayerInputHandler(),
                new SimplePlayerInputHandler(),
                new SimplePlayerInputHandler()
        );

        List<PlayerController> playerControllers = IntStream.range(0, players.size())
                .mapToObj(playerIndex -> new PlayerController(players.get(playerIndex), commandDispatcher, playerInputHandlers.get(playerIndex)))
                .collect(Collectors.toList());

        playerControllers.forEach(controller -> eventListenerRegistry.register(CardsDealt.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerHasTakenPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(StartedPlaying.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(CardPlayed.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(TrickWon.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(RoundEnded.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(GameEnded.class, controller::process));

        commandDispatcher.dispatch(new StartGame(lobbyId));
    }
}
