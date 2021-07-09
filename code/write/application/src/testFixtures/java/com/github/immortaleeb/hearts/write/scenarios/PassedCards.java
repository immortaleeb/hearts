package com.github.immortaleeb.hearts.write.scenarios;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.LinkedList;
import java.util.List;

public record PassedCards(List<CardPass> cardPasses) {

    public PassedCards pass(PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) {
        List<CardPass> cardPasses = new LinkedList<>(this.cardPasses);
        cardPasses.add(new CardPass(fromPlayer, toPlayer, cards));
        return new PassedCards(cardPasses);
    }

    public static PassedCards none() {
        return new PassedCards(new LinkedList<>());
    }

}
