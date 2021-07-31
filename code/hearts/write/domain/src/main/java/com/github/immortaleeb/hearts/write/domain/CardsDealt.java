package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;
import java.util.Map;

public final class CardsDealt implements GameEvent {

    private final GameId gameId;
    private final Map<PlayerId, List<Card>> playerHands;

    public CardsDealt(GameId gameId, Map<PlayerId, List<Card>> playerHands) {
        this.gameId = gameId;
        this.playerHands = playerHands;
    }

    public GameId gameId() {
        return gameId;
    }

    public Map<PlayerId, List<Card>> playerHands() {
        return playerHands;
    }

    @Override
    public String toString() {
        return "CardsDealt{" + "playerHands=" + playerHands + '}';
    }

}
