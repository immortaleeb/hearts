package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<GameEvent> raisedEvents = new ArrayList<>();

    public static Game startWith(List<PlayerId> players) {
        Game game = new Game();
        game.raiseEvent(new GameStarted(players));

        return game;
    }

    public List<GameEvent> raisedEvents() {
        return raisedEvents;
    }

    private void raiseEvent(GameEvent event) {
        this.raisedEvents.add(event);
    }

}
