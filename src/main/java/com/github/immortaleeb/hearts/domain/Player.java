package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Player {

    private final PlayerId id;
    private final List<Card> hand;
    private boolean passedCards;
    private boolean receivedCards;

    private Player(PlayerId id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public PlayerId id() {
        return id;
    }

    public List<Card> hand() {
        return hand;
    }

    public void markCardsPassed() {
        passedCards = true;
    }

    public boolean hasPassedCards() {
        return passedCards;
    }

    public void markCardsReceived() {
        receivedCards = true;
    }

    public boolean hasReceivedCards() {
        return receivedCards;
    }

    public static List<Player> listOf(List<PlayerId> playerIds) {
        return playerIds.stream()
                .map(Player::new)
                .collect(Collectors.toList());
    }

    public void giveCards(List<Card> cards) {
        this.hand.addAll(cards);
    }

}
