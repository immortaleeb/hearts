package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.application.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.List;
import java.util.Map;

public class PlayerController implements EventListener<GameEvent> {

    private final PlayerId playerId;
    private final CommandDispatcher commandDispatcher;

    public PlayerController(PlayerId playerId, CommandDispatcher commandDispatcher) {
        this.playerId = playerId;
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void process(GameEvent event) {
        if (event instanceof CardsDealt cardsDealt) {
            passCards(cardsDealt);
        }
    }

    private void passCards(CardsDealt cardsDealt) {
        Map<PlayerId, List<Card>> playerHands = cardsDealt.playerHands();
        List<Card> cardsToPass = playerHands.get(playerId).subList(0, 3);

        commandDispatcher.dispatch(new PassCards(cardsDealt.gameId(), playerId, cardsToPass));
    }

}
