package com.github.immortaleeb.common.application.api;

public interface CommandDispatcher {

    <R, C extends Command> R dispatch(C command);

}
