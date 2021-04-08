package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Players {

    private final List<Player> players;

    private Players(List<Player> players) {
        this.players = players;
    }

    public Player get(int index) {
        return players.get(index);
    }

    public int size() {
        return players.size();
    }

    public PlayerId choosePlayerWithOffset(PlayerId fromPlayer, int offset) {
        int fromPlayerIndex = indexOf(fromPlayer);
        int toPlayerIndex = (fromPlayerIndex + players.size() + offset) % players.size();

        return players.get(toPlayerIndex).id();
    }

    public boolean hasCards(PlayerId playerId, List<Card> cards) {
        return getPlayerById(playerId).hasCards(cards);
    }

    public boolean hasCard(PlayerId playerId, Card card) {
        return getPlayerById(playerId).hasCard(card);
    }

    public boolean anyCard(PlayerId playerId, Predicate<Card> predicate) {
        return getPlayerById(playerId).anyCard(predicate);
    }

    public void dealCards(Map<PlayerId, List<Card>> playerHands) {
        for (Map.Entry<PlayerId, List<Card>> entry : playerHands.entrySet()) {
            Player player = getPlayerById(entry.getKey());
            List<Card> cards = entry.getValue();

            player.dealCards(cards);
        }
    }

    public void giveCards(PlayerId player, List<Card> cards) {
        getPlayerById(player).giveCards(cards);
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

    public boolean hasReceivedCards(PlayerId playerId) {
        return getPlayerById(playerId).hasReceivedCards();
    }

    public boolean allReceivedCards() {
        return players.stream().allMatch(Player::hasReceivedCards);
    }

    public void markPlayedCard(PlayerId playerId) {
        getPlayerById(playerId).markPlayedCard();
    }

    public boolean allPlayedCards() {
        return players.stream().allMatch(Player::hasPlayedCard);
    }

    public PlayerId getPlayerWithCard(Card card) {
        return players.stream()
                .filter(player -> player.hasCard(card))
                .findFirst()
                .map(Player::id)
                .get();
    }

    public void takeCard(PlayerId playerId, Card card) {
        getPlayerById(playerId).takeCard(card);
    }

    public void takeCards(PlayerId player, List<Card> cards) {
        getPlayerById(player).takeCards(cards);
    }

    public List<PlayerId> toIds() {
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
