package com.github.immortaleeb.hearts.write.infrastructure.outgoing.projection;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.api.ProjectGameEvent;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.api.EventListener;

public class GameProjectionsHandler implements EventListener<GameEvent> {

    private final CommandDispatcher commandDispatcher;

    public GameProjectionsHandler(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void process(GameEvent event) {
        commandDispatcher.dispatch(new ProjectGameEvent(event));
    }

}
