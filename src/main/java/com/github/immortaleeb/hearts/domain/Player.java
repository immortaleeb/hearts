package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Player {

    private final PlayerId id;
    private final Hand hand;
    private boolean passedCards;
    private boolean receivedCards;

    private Player(PlayerId id) {
        this.id = id;
        this.hand = Hand.empty();
    }

    public PlayerId id() {
        return id;
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

    public void dealCards(List<Card> cards) {
        this.hand.receive(cards);
        this.passedCards = false;
        this.receivedCards = false;
    }

    public void giveCards(List<Card> cards) {
        this.hand.receive(cards);
    }

    public void takeCard(Card card) {
        takeCards(List.of(card));
    }

    public void takeCards(List<Card> cards) {
        this.hand.take(cards);
    }

    public boolean hasCards(List<Card> cards) {
        return hand.contains(cards);
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public boolean anyCard(Predicate<Card> predicate) {
        return hand.anyCard(predicate);
    }

    public static List<Player> listOf(List<PlayerId> playerIds) {
        return playerIds.stream()
                .map(Player::new)
                .collect(Collectors.toList());
    }
}
