package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

public interface EventListener<E extends GameEvent> {

    void process(E event);

}
