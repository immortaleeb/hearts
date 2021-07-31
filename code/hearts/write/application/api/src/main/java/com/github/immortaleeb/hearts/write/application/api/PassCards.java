package com.github.immortaleeb.hearts.write.application.api;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

import java.util.List;

public record PassCards(GameId gameId, PlayerId fromPlayer, List<Card> cards) implements Command {
}
