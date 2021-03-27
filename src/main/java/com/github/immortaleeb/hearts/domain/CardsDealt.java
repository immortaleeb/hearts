package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.Map;

public class CardsDealt implements GameEvent {

    private final Map<PlayerId, List<Card>> playerHands;

    public CardsDealt(Map<PlayerId, List<Card>> playerHands) {
        this.playerHands = playerHands;
    }

    public Map<PlayerId, List<Card>> playerHands() {
        return playerHands;
    }
}
