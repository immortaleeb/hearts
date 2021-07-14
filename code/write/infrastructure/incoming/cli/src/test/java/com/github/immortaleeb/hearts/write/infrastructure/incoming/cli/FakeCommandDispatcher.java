package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.application.*;
import com.github.immortaleeb.hearts.write.shared.GameId;

import java.util.ArrayList;
import java.util.List;

public class FakeCommandDispatcher implements CommandDispatcher {

    private final List<Command> dispatchedCommands = new ArrayList<>();

    @Override
    public GameId dispatch(StartGame startGame) {
        dispatchedCommands.add(startGame);
        return GameId.generate();
    }

    @Override
    public void dispatch(PassCards passCards) {
        dispatchedCommands.add(passCards);
    }

    @Override
    public void dispatch(PlayCard playCard) {
        dispatchedCommands.add(playCard);
    }

    public List<Command> dispatchedCommands() {
        return dispatchedCommands;
    }

    public Command lastDispatchedCommand() {
        if (dispatchedCommands.isEmpty()) {
            throw new IllegalStateException("No commands have been dispatched");
        }
        return dispatchedCommands.get(dispatchedCommands.size() - 1);
    }

}
