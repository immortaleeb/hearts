package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class EventDispatcher {

    private final EventListenerRegistry registry;
    private final Deque<GameEvent> undispatchedEvents = new LinkedList<>();
    private boolean dispatching;

    public EventDispatcher(EventListenerRegistry registry) {
        this.registry = registry;
    }

    public <E extends GameEvent> void dispatchAll(List<E> events) {
        undispatchedEvents.addAll(events);

        if (!dispatching) {
            dispatchUndispatchedEvents();
        }
    }

    private void dispatchUndispatchedEvents() {
        dispatching = true;

        while (!undispatchedEvents.isEmpty()) {
            dispatch(undispatchedEvents.poll());
        }

        dispatching = false;
    }

    private <E extends GameEvent> void dispatch(E event) {
        System.out.println(event);
        Class<E> eventClass = (Class<E>) event.getClass();
        registry.findListeners(eventClass).forEach(listener -> listener.process(event));
    }

}
