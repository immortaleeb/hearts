package com.github.immortaleeb.hearts.write.application.api;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.hearts.write.domain.GameEvent;

public record ProjectGameEvent(GameEvent gameEvent) implements Command {

}
