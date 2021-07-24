package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.api.*;
import com.github.immortaleeb.hearts.write.shared.GameId;

import java.util.ArrayList;
import java.util.List;

public class FakeCommandDispatcher implements CommandDispatcher {

    private final List<Command> dispatchedCommands = new ArrayList<>();

    @Override
    public <R, C extends Command> R dispatch(C command) {
        dispatchedCommands.add(command);

        if (command instanceof StartGame) {
            return (R) GameId.generate();
        }

        return null;
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
