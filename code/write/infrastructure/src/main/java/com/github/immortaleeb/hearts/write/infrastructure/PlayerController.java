package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.application.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.application.PlayCard;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Rank;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerController implements EventListener<GameEvent> {

    private static final Card OPENING_CARD = Card.of(Suite.CLUBS, Rank.TWO);

    private final PlayerId playerId;
    private final CommandDispatcher commandDispatcher;
    private final List<Card> hand;
    private int roundNumber;

    public PlayerController(PlayerId playerId, CommandDispatcher commandDispatcher) {
        this.playerId = playerId;
        this.commandDispatcher = commandDispatcher;
        this.hand = new ArrayList<>();
        this.roundNumber = 0;
    }

    @Override
    public void process(GameEvent event) {
        if (event instanceof CardsDealt cardsDealt) {
            incrementRoundNumber();
            updateHand(cardsDealt);
            passCardsIfNecessary(cardsDealt);
        } else if (event instanceof PlayerPassedCards playerPassedCards) {
            removeCardsFromHand(playerPassedCards);
        } else if (event instanceof PlayerHasTakenPassedCards playerHasTakenPassedCards) {
            addCardsToHand(playerHasTakenPassedCards);
        } else if (event instanceof StartedPlaying startedPlaying) {
            playOpeningCard(startedPlaying);
        } else if (event instanceof CardPlayed cardPlayed) {
            playFirstPlayableCard(cardPlayed);
            removeCardFromHand(cardPlayed);
        } else if (event instanceof TrickWon trickWon) {
            playFirstCard(trickWon);
        }
    }

    private void incrementRoundNumber() {
        roundNumber++;
    }

    private void addCardsToHand(PlayerHasTakenPassedCards playerHasTakenPassedCards) {
        if (playerHasTakenPassedCards.toPlayer().equals(playerId)) {
            hand.addAll(playerHasTakenPassedCards.cards());
        }
    }

    private void playFirstCard(TrickWon trickWon) {
        if (trickWon.wonBy().equals(playerId) && !hand.isEmpty()) {
            Card firstCard = hand.get(0);
            commandDispatcher.dispatch(new PlayCard(trickWon.gameId(), playerId, firstCard));
        }
    }

    private void removeCardFromHand(CardPlayed cardPlayed) {
        if (cardPlayed.playedBy().equals(playerId)) {
            hand.remove(cardPlayed.card());
        }
    }

    private void removeCardsFromHand(PlayerPassedCards playerPassedCards) {
        if (playerPassedCards.fromPlayer().equals(playerId)) {
            hand.removeAll(playerPassedCards.passedCards());
        }
    }

    private void passCardsIfNecessary(CardsDealt cardsDealt) {
        if (shouldPassCards()) {
            passCards(cardsDealt);
        }
    }

    private void passCards(CardsDealt cardsDealt) {
        List<Card> cardsToPass = IntStream.range(0, 3)
                .mapToObj(hand::get)
                .collect(Collectors.toList());

        commandDispatcher.dispatch(new PassCards(cardsDealt.gameId(), playerId, cardsToPass));
    }

    private boolean shouldPassCards() {
        return (roundNumber - 1) % 4 != 3;
    }

    private void updateHand(CardsDealt cardsDealt) {
        Map<PlayerId, List<Card>> playerHands = cardsDealt.playerHands();
        hand.addAll(playerHands.get(playerId));
    }

    private void playOpeningCard(StartedPlaying startedPlaying) {
        if (startedPlaying.leadingPlayer().equals(playerId)) {
            commandDispatcher.dispatch(new PlayCard(startedPlaying.gameId(), playerId, OPENING_CARD));
        }
    }

    private void playFirstPlayableCard(CardPlayed cardPlayed) {
        boolean playerIsNextLeadingPlayer = cardPlayed.nextLeadingPlayer().stream().anyMatch(playerId::equals);

        if (playerIsNextLeadingPlayer) {
            Card firstPlayableCard = cardPlayed.validCardsForNextPlayer().get(0);
            commandDispatcher.dispatch(new PlayCard(cardPlayed.gameId(), playerId, firstPlayableCard));
        }
    }

}
