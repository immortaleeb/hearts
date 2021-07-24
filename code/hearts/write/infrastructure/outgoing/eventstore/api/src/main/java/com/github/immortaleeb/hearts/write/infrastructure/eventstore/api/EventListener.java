package com.github.immortaleeb.hearts.write.infrastructure.eventstore.api;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

public interface EventListener<E extends GameEvent> {

    void process(E event);

}
