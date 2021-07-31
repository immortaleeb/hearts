package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;

public final class PlayerPassedCards implements GameEvent {

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

    @Override
    public String toString() {
        return "PlayerPassedCards{" + "fromPlayer=" + fromPlayer + ", toPlayer=" + toPlayer + ", passedCards=" +
                passedCards + '}';
    }

}
