package com.github.immortaleeb.hearts;

import com.github.immortaleeb.hearts.application.CommandDispatcher;
import com.github.immortaleeb.hearts.application.StartGame;
import com.github.immortaleeb.hearts.domain.GameRepository;
import com.github.immortaleeb.hearts.infrastructure.InMemoryGameRepository;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        GameRepository gameRepository = new InMemoryGameRepository(true);
        CommandDispatcher dispatcher = new CommandDispatcher(gameRepository);

        List<PlayerId> players = Arrays.asList(
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate(),
                PlayerId.generate());

        dispatcher.dispatch(new StartGame(players));
    }
}
