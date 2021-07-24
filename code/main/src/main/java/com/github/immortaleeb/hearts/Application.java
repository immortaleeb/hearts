package com.github.immortaleeb.hearts;

import com.github.immortaleeb.common.application.api.CommandHandlerDispatcher;
import com.github.immortaleeb.common.application.api.CommandHandlerRegistry;
import com.github.immortaleeb.hearts.write.application.usecase.*;
import com.github.immortaleeb.hearts.write.application.api.StartGame;
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
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) {
        EventListenerRegistry eventListenerRegistry = new EventListenerRegistry();
        EventDispatcher eventDispatcher = new EventDispatcher(eventListenerRegistry);
        EventStore eventStore = new InMemoryEventStore(eventDispatcher);
        GameRepository gameRepository = new EventSourcedGameRepository(eventStore);

        CommandHandlerRegistry commandHandlerRegistry = new CommandHandlerRegistry();
        CommandHandlerDispatcher dispatcher = new CommandHandlerDispatcher(commandHandlerRegistry);

        GameCommandHandlers.registerAll(commandHandlerRegistry, gameRepository);

        List<PlayerId> players = Arrays.asList(
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate());

        List<PlayerInputHandler> playerInputHandlers = List.of(
                new StdinPlayerInputHandler(),
                new SimplePlayerInputHandler(),
                new SimplePlayerInputHandler(),
                new SimplePlayerInputHandler()
        );

        List<PlayerController> playerControllers = IntStream.range(0, players.size())
                .mapToObj(playerIndex -> new PlayerController(players.get(playerIndex), dispatcher, playerInputHandlers.get(playerIndex)))
                .collect(Collectors.toList());

        playerControllers.forEach(controller -> eventListenerRegistry.register(CardsDealt.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerHasTakenPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(StartedPlaying.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(CardPlayed.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(TrickWon.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(RoundEnded.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(GameEnded.class, controller::process));

        dispatcher.dispatch(new StartGame(players));

    }
}
