package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.Optional;

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

    public Optional<PlayerId> nextLeadingPlayer() {
        return Optional.ofNullable(nextLeadingPlayer);
    }

    @Override
    public String toString() {
        return "CardPlayed{" + "playedBy=" + playedBy + ", card=" + card + ", nextLeadingPlayer=" + nextLeadingPlayer +
                '}';
    }

}
