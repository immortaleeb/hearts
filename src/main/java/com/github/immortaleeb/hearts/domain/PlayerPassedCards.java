package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;

public class PlayerPassedCards implements GameEvent {

    private final PlayerId fromPlayer;
    private final PlayerId toPlayer;
    private final List<Card> passedCards;

    public PlayerPassedCards(PlayerId fromPlayer, PlayerId toPlayer, List<Card> passedCards) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.passedCards = passedCards;
    }

    public PlayerId fromPlayer() {
        return fromPlayer;
    }

    public PlayerId toPlayer() {
        return toPlayer;
    }

    public List<Card> passedCards() {
        return passedCards;
    }
}