package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.Rank;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Deck {

    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> takeCards(int cardsToTake) {
        return IntStream.range(0, cardsToTake)
                .mapToObj(i -> takeCard())
                .collect(Collectors.toList());
    }

    private Card takeCard() {
        return cards.remove(cards.size() - 1);
    }

    public Deck shuffle() {
        Collections.shuffle(cards);
        return this;
    }

    public static Deck standard() {
        List<Card> deck = new ArrayList<>();

        for (Suite suite : Suite.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suite, rank));
            }
        }

        return new Deck(deck);
    }
}
