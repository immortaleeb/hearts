package com.github.immortaleeb.common.application.api;

import java.util.Optional;

public class CommandHandlerDispatcher implements CommandDispatcher {

    private final CommandHandlerRegistry commandHandlerRegistry;

    public CommandHandlerDispatcher(CommandHandlerRegistry commandHandlerRegistry) {
        this.commandHandlerRegistry = commandHandlerRegistry;
    }

    public <R, C extends Command> R dispatch(C command) {
        Optional<CommandHandler<R, C>> commandHandler = commandHandlerRegistry.findHandlerFor(command);

        if (commandHandler.isEmpty()) {
            throw new RuntimeException("No command handler found for command of type " + command.getClass());
        }

        return commandHandler.get().handle(command);
    }

}

