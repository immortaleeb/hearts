package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.CardsNotInHand;

import java.util.ArrayList;
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

}
