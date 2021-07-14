package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.write.application.CommandHandlerDispatcher;
import com.github.immortaleeb.hearts.write.application.StartGame;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.infrastructure.*;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        EventListenerRegistry eventListenerRegistry = new EventListenerRegistry();
        EventDispatcher eventDispatcher = new EventDispatcher(eventListenerRegistry);
        EventStore eventStore = new InMemoryEventStore(eventDispatcher);
        GameRepository gameRepository = new InMemoryGameRepository(eventStore);
        CommandHandlerDispatcher dispatcher = new CommandHandlerDispatcher(gameRepository);

        List<PlayerId> players = Arrays.asList(
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate());

        List<PlayerController> playerControllers = players.stream()
                .map(player -> new PlayerController(player, dispatcher, new SimplePlayerInputHandler()))
                .collect(Collectors.toList());

        playerControllers.forEach(controller -> eventListenerRegistry.register(CardsDealt.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(PlayerHasTakenPassedCards.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(StartedPlaying.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(CardPlayed.class, controller::process));
        playerControllers.forEach(controller -> eventListenerRegistry.register(TrickWon.class, controller::process));

        dispatcher.dispatch(new StartGame(players));

    }
}
