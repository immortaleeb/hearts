package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.List;

public record PlayerHasTakenPassedCards(GameId gameId, PlayerId fromPlayer, PlayerId toPlayer, List<Card> cards) implements GameEvent {
}
