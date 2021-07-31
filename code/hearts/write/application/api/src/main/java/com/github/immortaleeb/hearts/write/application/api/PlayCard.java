package com.github.immortaleeb.hearts.write.application.api;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.common.shared.PlayerId;

public record PlayCard(GameId gameId, PlayerId player, Card card) implements Command {
}
