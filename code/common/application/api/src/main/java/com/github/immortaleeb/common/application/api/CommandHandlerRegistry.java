package com.github.immortaleeb.common.application.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandHandlerRegistry {

    private final Map<Class<? extends Command>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();

    public <R, C extends Command> Optional<CommandHandler<R, C>> findHandlerFor(C command) {
        return Optional.ofNullable((CommandHandler<R, C>) commandHandlers.get(command.getClass()));
    }

    public <R, C extends Command> void register(Class<C> command, CommandHandler<R, C> commandHandler) {
        commandHandlers.put(command, commandHandler);
    }

}
