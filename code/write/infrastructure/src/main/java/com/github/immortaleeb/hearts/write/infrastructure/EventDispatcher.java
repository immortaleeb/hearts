package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

public class EventDispatcher {

    private final EventListenerRegistry registry;

    public EventDispatcher(EventListenerRegistry registry) {
        this.registry = registry;
    }

    public <E extends GameEvent> void dispatch(E event) {
        System.out.println(event);
        Class<E> eventClass = (Class<E>) event.getClass();
        registry.findListeners(eventClass).forEach(listener -> listener.process(event));
    }

}
