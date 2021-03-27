package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;

public class PassCards {

    private final GameId gameId;
    private final PlayerId fromPlayer;
    private final List<Card> cards;

    public PassCards(GameId gameId, PlayerId fromPlayer, List<Card> cards) {
        this.gameId = gameId;
        this.fromPlayer = fromPlayer;
        this.cards = cards;
    }

    public GameId gameId() {
        return gameId;
    }

    public PlayerId fromPlayer() {
        return fromPlayer;
    }

    public List<Card> cards() {
        return cards;
    }

}
