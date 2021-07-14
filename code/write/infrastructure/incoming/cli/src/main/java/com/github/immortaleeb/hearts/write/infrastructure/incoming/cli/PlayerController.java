package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.application.CommandDispatcher;
import com.github.immortaleeb.hearts.write.application.PassCards;
import com.github.immortaleeb.hearts.write.application.PlayCard;
import com.github.immortaleeb.hearts.write.domain.*;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.api.EventListener;
import com.github.immortaleeb.hearts.write.shared.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerController implements EventListener<GameEvent> {

    private static final Card OPENING_CARD = Card.of(Suite.CLUBS, Rank.TWO);

    private final CommandDispatcher commandDispatcher;
    private final PlayerInputHandler playerInputHandler;

    private final PlayerId playerId;
    private final List<Card> hand;
    private int roundNumber;

    public PlayerController(PlayerId playerId, CommandDispatcher commandDispatcher, PlayerInputHandler playerInputHandler) {
        this.commandDispatcher = commandDispatcher;
        this.playerInputHandler = playerInputHandler;

        this.playerId = playerId;
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
            playCardFromHand(cardPlayed);
            removeCardFromHand(cardPlayed);
        } else if (event instanceof TrickWon trickWon) {
            playCardFromHand(trickWon);
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

    private void playCardFromHand(TrickWon trickWon) {
        if (trickWon.wonBy().equals(playerId) && !hand.isEmpty()) {
            playCard(trickWon.gameId(), hand);
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
        List<Card> cardsToPass = playerInputHandler.chooseCardsToPass(hand);
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
            playCard(startedPlaying.gameId(), List.of(OPENING_CARD));
        }
    }

    private void playCardFromHand(CardPlayed cardPlayed) {
        boolean playerIsNextLeadingPlayer = cardPlayed.nextLeadingPlayer().stream().anyMatch(playerId::equals);

        if (playerIsNextLeadingPlayer) {
            playCard(cardPlayed.gameId(), cardPlayed.validCardsForNextPlayer());
        }
    }

    private void playCard(GameId gameId, List<Card> playableCards) {
        Card cardToPlay = playerInputHandler.chooseCardToPlay(playableCards);
        commandDispatcher.dispatch(new PlayCard(gameId, playerId, cardToPlay));
    }

}
