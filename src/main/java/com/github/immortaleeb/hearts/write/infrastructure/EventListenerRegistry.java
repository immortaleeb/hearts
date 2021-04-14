package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EventListenerRegistry {

    private final Map<Class<?>, EventListener<?>> eventListenerMap = new HashMap<>();

    public <E extends GameEvent> void register(Class<E> eventClass, EventListener<E> listener) {
        eventListenerMap.put(eventClass, listener);
    }

    public <E extends GameEvent> Optional<EventListener<E>> findListener(Class<E> eventClass) {
        return Optional.ofNullable((EventListener<E>) eventListenerMap.get(eventClass));
    }

}
