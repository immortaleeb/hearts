package com.github.immortaleeb.hearts.application;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.GameId;
import com.github.immortaleeb.hearts.shared.PlayerId;

public class PlayCard {

    private final GameId gameId;
    private final PlayerId player;
    private final Card card;

    public PlayCard(GameId gameId, PlayerId player, Card card) {
        this.gameId = gameId;
        this.player = player;
        this.card = card;
    }

    public GameId gameId() {
        return gameId;
    }

    public PlayerId player() {
        return player;
    }

    public Card card() {
        return card;
    }

}
