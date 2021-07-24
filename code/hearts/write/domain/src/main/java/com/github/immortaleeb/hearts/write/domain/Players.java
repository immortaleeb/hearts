package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Players {

    private final List<Player> players;

    private Players(List<Player> players) {
        this.players = players;
    }

    public int size() {
        return players.size();
    }

    public PlayerId choosePlayerWithOffset(PlayerId fromPlayer, int offset) {
        int fromPlayerIndex = indexOf(fromPlayer);
        int toPlayerIndex = (fromPlayerIndex + players.size() + offset) % players.size();

        return players.get(toPlayerIndex).id();
    }

    private int indexOf(PlayerId playerId) {
        return IntStream.range(0, players.size())
                .filter(i -> players.get(i).id().equals(playerId))
                .findFirst()
                .orElse(-1);
    }

    public Player getPlayerById(PlayerId playerId) {
        return where(player -> player.id().equals(playerId));
    }

    public void forEach(Consumer<Player> playerConsumer) {
        players.forEach(playerConsumer);
    }

    public Player where(Predicate<Player> predicate) {
        return players.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find player matching predicate"));
    }

    public boolean all(Predicate<Player> predicate) {
        return players.stream()
                .allMatch(predicate);
    }

    public List<PlayerId> ids() {
        return players.stream()
                .map(Player::id)
                .collect(Collectors.toList());
    }

    public static Players of(List<PlayerId> playerIds) {
        return new Players(Player.listOf(playerIds));
    }

}
