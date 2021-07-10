package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.CardsNotInHand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

class Hand {

    private final List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public static Hand empty() {
        return new Hand(new ArrayList<>());
    }

    public void receive(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public void take(Card card) {
        take(List.of(card));
    }

    public void take(List<Card> cards) {
        if (!contains(cards)) {
            throw new CardsNotInHand();
        }

        this.cards.removeAll(cards);
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public boolean contains(List<Card> cards) {
        return cards.stream()
                .allMatch(this::contains);
    }

    public boolean anyCard(Predicate<Card> predicate) {
        return cards.stream()
                .anyMatch(predicate);
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

}
