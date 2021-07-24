package com.github.immortaleeb.common.application.api;

public interface NoResultCommandHandler<C extends Command> extends CommandHandler<Void, C> {

    default Void handle(C command) {
        handleNoResult(command);
        return null;
    }

    void handleNoResult(C command);

}
