package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class Players {

    private final List<Player> players;

    private Players(List<Player> players) {
        this.players = players;
    }

    public Player get(int index) {
        return players.get(index);
    }

    public PlayerId choosePlayerWithOffset(PlayerId fromPlayer, int offset) {
        int fromPlayerIndex = indexOf(fromPlayer);
        int toPlayerIndex = (fromPlayerIndex + players.size() + offset) % players.size();

        return players.get(toPlayerIndex).id();
    }

    public List<Card> handOf(PlayerId playerId) {
        return getPlayerById(playerId).hand();
    }

    public void dealCards(Map<PlayerId, List<Card>> playerHands) {
        for (Map.Entry<PlayerId, List<Card>> entry : playerHands.entrySet()) {
            Player player = getPlayerById(entry.getKey());
            player.giveCards(entry.getValue());
        }
    }

    public void markCardsPassed(PlayerId playerId) {
        getPlayerById(playerId).markCardsPassed();
    }

    public boolean hasPassedCards(PlayerId playerId) {
        return getPlayerById(playerId).hasPassedCards();
    }

    public void markCardsReceived(PlayerId playerId) {
        getPlayerById(playerId).markCardsReceived();
    }

    public boolean allReceivedCards() {
        return players.stream().allMatch(Player::hasReceivedCards);
    }

    private int indexOf(PlayerId playerId) {
        return IntStream.range(0, players.size())
                .filter(i -> players.get(i).id().equals(playerId))
                .findFirst()
                .orElse(-1);
    }

    private Player getPlayerById(PlayerId playerId) {
        return players.stream()
                .filter(player -> player.id().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown player with id " + playerId));
    }

    public static Players of(List<PlayerId> playerIds) {
        return new Players(Player.listOf(playerIds));
    }

}
