package com.github.immortaleeb.hearts.write.application;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

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
