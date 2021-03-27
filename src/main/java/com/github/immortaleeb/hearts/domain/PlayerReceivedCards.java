package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;

public class PlayerReceivedCards implements GameEvent {

    private final PlayerId fromPlayer;
    private final PlayerId toPlayer;
    private final List<Card> cards;

    public PlayerReceivedCards(PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.cards = cards;
    }

    public PlayerId fromPlayer() {
        return fromPlayer;
    }

    public PlayerId toPlayer() {
        return toPlayer;
    }

    public List<Card> cards() {
        return cards;
    }
}
