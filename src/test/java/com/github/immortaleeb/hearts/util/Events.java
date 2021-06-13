package com.github.immortaleeb.hearts.util;

import com.github.immortaleeb.hearts.write.domain.GameEvent;
import jdk.jfr.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Events {

    private final List<GameEvent> events;

    public Events() {
        this(new ArrayList<>());
    }

    private Events(List<GameEvent> events) {
        this.events = events;
    }

    public Events add(GameEvent event) {
        events.add(event);
        return this;
    }

    public Events addAll(GameEvent... events) {
        return addAll(Arrays.asList(events));
    }

    public Events addAll(Events events) {
        return addAll(events.events);
    }

    public Events addAll(List<? extends GameEvent> events) {
        this.events.addAll(events);
        return this;
    }

    public List<GameEvent> toList() {
        return events;
    }

    public static Events of(GameEvent... events) {
        return of(Arrays.asList(events));
    }

    public static <T extends GameEvent> Events of(List<T> list) {
        return new Events(new ArrayList<>(list));
    }

    public static Events none() {
        return new Events();
    }

}
