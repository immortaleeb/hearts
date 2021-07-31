package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.common.application.api.CommandDispatcher;
import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.application.api.PassCards;
import com.github.immortaleeb.hearts.write.application.api.PlayCard;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.List;

public class InMemoryHeartsWriteApi implements HeartsWriteApi {

    private final CommandDispatcher commandDispatcher;

    public InMemoryHeartsWriteApi(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void passCards(GameId gameId, PlayerId playerId, List<Card> cardsToPass) {
        commandDispatcher.dispatch(new PassCards(gameId, playerId, cardsToPass));
    }

    @Override
    public void playCard(GameId gameId, PlayerId playerId, Card cardToPlay) {
        commandDispatcher.dispatch(new PlayCard(gameId, playerId, cardToPlay));
    }

}
