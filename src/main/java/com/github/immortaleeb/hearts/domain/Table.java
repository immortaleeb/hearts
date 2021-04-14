package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Table {

    private final Map<PlayerId, List<Card>> passedCards;

    public Table() {
        this.passedCards = new HashMap<>();
    }

    public void passCardsTo(PlayerId toPlayer, List<Card> cards) {
        this.passedCards.put(toPlayer, cards);
    }

    public boolean hasCardsPassedTo(PlayerId toPlayer) {
        return this.passedCards.containsKey(toPlayer);
    }

    public List<Card> cardsPassedTo(PlayerId toPlayer) {
        return this.passedCards.get(toPlayer);
    }

    public void takeCardsPassedTo(PlayerId toPlayer) {
        this.passedCards.remove(toPlayer);
    }

}
