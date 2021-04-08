package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

public final class CardPlayed implements GameEvent {

    private final PlayerId playedBy;
    private final Card card;
    private final PlayerId nextLeadingPlayer;

    public CardPlayed(PlayerId playedBy, Card card, PlayerId nextLeadingPlayer) {
        this.playedBy = playedBy;
        this.card = card;
        this.nextLeadingPlayer = nextLeadingPlayer;
    }

    public PlayerId playedBy() {
        return playedBy;
    }

    public Card card() {
        return card;
    }

    public PlayerId nextLeadingPlayer() {
        return nextLeadingPlayer;
    }

    @Override
    public String toString() {
        return "CardPlayed{" + "playedBy=" + playedBy + ", card=" + card + ", nextLeadingPlayer=" + nextLeadingPlayer +
                '}';
    }

}
