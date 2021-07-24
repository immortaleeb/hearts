package com.github.immortaleeb.hearts.write.application.api;

import com.github.immortaleeb.common.application.api.Command;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.GameId;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

public record PlayCard(GameId gameId, PlayerId player, Card card) implements Command {
}
