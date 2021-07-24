package com.github.immortaleeb.hearts.write.shared;

public class WrongNumberOfPlayers extends RuntimeException {
    public WrongNumberOfPlayers(String message) {
        super(message);
    }
}
