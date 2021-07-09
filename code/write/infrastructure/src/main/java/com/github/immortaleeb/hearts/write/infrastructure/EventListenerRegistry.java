package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.domain.GameEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class EventListenerRegistry {

    private final Map<Class<?>, List<EventListener<?>>> eventListenerMap = new HashMap<>();

    public <E extends GameEvent> void register(Class<E> eventClass, EventListener<E> listener) {
        List<EventListener<?>> listeners = eventListenerMap.getOrDefault(eventClass, new ArrayList<>());
        listeners.add(listener);
        eventListenerMap.put(eventClass, listeners);
    }

    public <E extends GameEvent> List<EventListener<E>> findListeners(Class<E> eventClass) {
        return (List) eventListenerMap.getOrDefault(eventClass, emptyList());
    }

}
