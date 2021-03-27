package com.github.immortaleeb.hearts.shared;

import java.util.UUID;

public class PlayerId {

    private final UUID uuid;

    private PlayerId(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID asUuid() {
        return uuid;
    }

    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID());
    }
}
