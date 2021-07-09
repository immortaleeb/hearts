package com.github.immortaleeb.hearts.write.application;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;

public record PassCards(GameId gameId, PlayerId fromPlayer, List<Card> cards) implements Command {
}
