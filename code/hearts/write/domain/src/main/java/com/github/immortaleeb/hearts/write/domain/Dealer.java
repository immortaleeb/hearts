package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dealer {

    private final int cardsPerHand;

    public Dealer(int cardsPerHand) {
        this.cardsPerHand = cardsPerHand;
    }

    public Map<PlayerId, List<Card>> deal(List<PlayerId> toPlayers) {
        return deal(Deck.standard(), toPlayers);
    }

    public Map<PlayerId, List<Card>> deal(Deck deck, List<PlayerId> toPlayers) {
        Map<PlayerId, List<Card>> playerHands = new HashMap<>();

        deck = deck.shuffle();

        for (PlayerId player : toPlayers) {
            playerHands.put(player, deck.takeCards(cardsPerHand));
        }

        return playerHands;
    }

}
