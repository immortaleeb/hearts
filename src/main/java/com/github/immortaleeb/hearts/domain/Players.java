package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.Map;
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

    public void takeDealtCards(Map<PlayerId, List<Card>> playerHands) {
        playerHands.forEach(this::takeDealtCards);
    }

    private void takeDealtCards(PlayerId playerId, List<Card> cards) {
        Player player = getPlayerById(playerId);
        player.takeDealtCards(cards);
    }

    public boolean allPassedCards() {
        return players.stream().allMatch(Player::hasPassedCards);
    }

    public PlayerId getPlayerWithCard(Card card) {
        return players.stream()
                .filter(player -> player.hand().contains(card))
                .findFirst()
                .map(Player::id)
                .get();
    }

    public List<PlayerId> ids() {
        return players.stream()
                .map(Player::id)
                .collect(Collectors.toList());
    }

    private int indexOf(PlayerId playerId) {
        return IntStream.range(0, players.size())
                .filter(i -> players.get(i).id().equals(playerId))
                .findFirst()
                .orElse(-1);
    }

    public Player getPlayerById(PlayerId playerId) {
        return players.stream()
                .filter(player -> player.id().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown player with id " + playerId));
    }

    public static Players of(List<PlayerId> playerIds) {
        return new Players(Player.listOf(playerIds));
    }

}
