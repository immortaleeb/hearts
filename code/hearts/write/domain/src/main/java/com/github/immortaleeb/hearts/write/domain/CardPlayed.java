package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.Optional;

public final class CardPlayed implements GameEvent {

    private final GameId gameId;
    private final PlayerId playedBy;
    private final Card card;
    private final PlayerId nextLeadingPlayer;
    private final List<Card> validCardsForNextPlayer;

    public CardPlayed(GameId gameId, PlayerId playedBy, Card card, PlayerId nextLeadingPlayer, List<Card> validCardsForNextPlayer) {
        this.gameId = gameId;
        this.playedBy = playedBy;
        this.card = card;
        this.nextLeadingPlayer = nextLeadingPlayer;
        this.validCardsForNextPlayer = validCardsForNextPlayer;
    }

    public GameId gameId() {
        return gameId;
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

    public List<Card> validCardsForNextPlayer() {
        return validCardsForNextPlayer;
    }

    @Override
    public String toString() {
        return "CardPlayed{" +
                "gameId=" + gameId +
                ", playedBy=" + playedBy +
                ", card=" + card +
                ", nextLeadingPlayer=" + nextLeadingPlayer +
                ", validCardsForNextPlayer=" + validCardsForNextPlayer +
                '}';
    }
}
