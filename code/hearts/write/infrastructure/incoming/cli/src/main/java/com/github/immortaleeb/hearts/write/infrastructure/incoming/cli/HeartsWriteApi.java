package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;

import java.util.List;

public interface HeartsWriteApi {

    void passCards(GameId gameId, PlayerId playerId, List<Card> cardsToPass);

    void playCard(GameId gameId, PlayerId playerId, Card cardToPlay);

}
