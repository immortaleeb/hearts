package com.github.immortaleeb.hearts.write.application;

import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;

public class StartGame implements Command {

    private final List<PlayerId> players;

    public StartGame(List<PlayerId> players) {
        this.players = players;
    }

    public List<PlayerId> players() {
        return players;
    }

}
