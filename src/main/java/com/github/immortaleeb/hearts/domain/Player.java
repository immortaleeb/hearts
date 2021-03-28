package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Player {

    private final PlayerId id;
    private final List<Card> hand;
    private boolean passedCards;
    private boolean receivedCards;
    private boolean playedCard;

    private Player(PlayerId id) {
        this.id = id;
        this.hand = new ArrayList<>();
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

    public void giveCards(List<Card> cards) {
        this.hand.addAll(cards);
    }

    public void takeCards(List<Card> cards) {
        if (!hasCards(cards)) {
            throw new CardsNotInHand();
        }

        this.hand.removeAll(cards);
    }

    public boolean hasCards(List<Card> cards) {
        return cards.stream()
            .allMatch(this::hasCard);
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public boolean anyCard(Predicate<Card> predicate) {
        return hand.stream()
                .anyMatch(predicate);
    }

    public void markPlayedCard() {
        playedCard = true;
    }

    public boolean hasPlayedCard() {
        return playedCard;
    }

    public static List<Player> listOf(List<PlayerId> playerIds) {
        return playerIds.stream()
                .map(Player::new)
                .collect(Collectors.toList());
    }
}
