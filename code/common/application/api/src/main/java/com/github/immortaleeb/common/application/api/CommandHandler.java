package com.github.immortaleeb.common.application.api;

public interface CommandHandler<R, C extends Command> {

    R handle(C command);

}
