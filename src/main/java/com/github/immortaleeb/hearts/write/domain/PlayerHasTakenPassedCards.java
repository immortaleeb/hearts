package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;

public final class PlayerHasTakenPassedCards implements GameEvent {

    private final PlayerId fromPlayer;
    private final PlayerId toPlayer;
    private final List<Card> cards;

    public PlayerHasTakenPassedCards(PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) {
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

    @Override
    public String toString() {
        return "PlayerHasTakenPassedCards{" + "fromPlayer=" + fromPlayer + ", toPlayer=" + toPlayer + ", cards=" + cards +
                '}';
    }

}
