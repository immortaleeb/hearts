package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;

public class StartGame {

    private final List<PlayerId> players;

    public StartGame(List<PlayerId> players) {
        this.players = players;
    }

    public List<PlayerId> players() {
        return players;
    }

}
