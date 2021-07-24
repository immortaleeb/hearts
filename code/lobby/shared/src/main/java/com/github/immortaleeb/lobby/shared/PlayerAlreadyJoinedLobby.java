package com.github.immortaleeb.lobby.shared;

public class PlayerAlreadyJoinedLobby extends RuntimeException {

    public PlayerAlreadyJoinedLobby(String message) {
        super(message);
    }

}
