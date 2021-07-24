package com.github.immortaleeb.common.shared;

import java.util.Objects;
import java.util.UUID;

public class PlayerId {

    private final UUID uuid;

    private PlayerId(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID asUuid() {
        return uuid;
    }

    public String asString() {
        return uuid.toString();
    }

    public static PlayerId generate() {
        return of(UUID.randomUUID());
    }

    public static PlayerId of(String uuid) {
        return of(UUID.fromString(uuid));
    }

    private static PlayerId of(UUID uuid) {
        return new PlayerId(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerId playerId = (PlayerId) o;
        return Objects.equals(uuid, playerId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
