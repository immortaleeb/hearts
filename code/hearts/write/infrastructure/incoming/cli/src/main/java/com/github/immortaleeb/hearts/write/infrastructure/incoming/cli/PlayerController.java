package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.common.shared.PlayerId;
import com.github.immortaleeb.hearts.write.domain.CardPlayed;
import com.github.immortaleeb.hearts.write.domain.CardsDealt;
import com.github.immortaleeb.hearts.write.domain.GameEnded;
import com.github.immortaleeb.hearts.write.domain.GameEvent;
import com.github.immortaleeb.hearts.write.domain.PlayerHasTakenPassedCards;
import com.github.immortaleeb.hearts.write.domain.PlayerPassedCards;
import com.github.immortaleeb.hearts.write.domain.RoundEnded;
import com.github.immortaleeb.hearts.write.domain.StartedPlaying;
import com.github.immortaleeb.hearts.write.domain.TrickWon;
import com.github.immortaleeb.hearts.write.infrastructure.eventstore.api.EventListener;
import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hears.common.shared.GameId;
import com.github.immortaleeb.hears.common.shared.Rank;
import com.github.immortaleeb.hears.common.shared.Suite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerController implements EventListener<GameEvent> {

    private static final Card OPENING_CARD = Card.of(Suite.CLUBS, Rank.TWO);

    private final HeartsWriteApi heartsWriteApi;
    private final PlayerInputHandler playerInputHandler;

    private final PlayerId playerId;
    private final List<Card> hand;
    private int roundNumber;

    public PlayerController(PlayerId playerId, HeartsWriteApi heartsWriteApi, PlayerInputHandler playerInputHandler) {
        this.heartsWriteApi = heartsWriteApi;
        this.playerInputHandler = playerInputHandler;

        this.playerId = playerId;
        this.hand = new ArrayList<>();
        this.roundNumber = 0;
    }

    @Override
    public void process(GameEvent event) {
        if (event instanceof CardsDealt cardsDealt) {
            showDealtCards(cardsDealt);
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
            showPlayedCard(cardPlayed);
            playCardFromHand(cardPlayed);
            removeCardFromHand(cardPlayed);
        } else if (event instanceof TrickWon trickWon) {
            showPlayerWonTrick(trickWon);
            playCardFromHand(trickWon);
        } else if (event instanceof RoundEnded roundEnded) {
            showRoundEnded(roundEnded);
        } else if (event instanceof GameEnded gameEnded) {
            showGameEnded(gameEnded);
        }
    }

    private void showGameEnded(GameEnded gameEnded) {
        playerInputHandler.showGameEnded(gameEnded.scores());
    }

    private void showRoundEnded(RoundEnded roundEnded) {
        playerInputHandler.showRoundEnded(roundEnded.scores());
    }

    private void showPlayerWonTrick(TrickWon trickWon) {
        playerInputHandler.showPlayerWonTrick(trickWon.wonBy());
    }

    private void showPlayedCard(CardPlayed cardPlayed) {
        playerInputHandler.showPlayedCard(cardPlayed.playedBy(), cardPlayed.card());
    }

    private void showDealtCards(CardsDealt cardsDealt) {
        List<Card> playerHand = getCardsForPlayer(cardsDealt);
        playerInputHandler.showDealtCards(playerHand);
    }

    private void incrementRoundNumber() {
        roundNumber++;
    }

    private void addCardsToHand(PlayerHasTakenPassedCards playerHasTakenPassedCards) {
        if (playerHasTakenPassedCards.toPlayer().equals(playerId)) {
            hand.addAll(playerHasTakenPassedCards.cards());
            playerInputHandler.showReceivedCards(playerHasTakenPassedCards.fromPlayer(), playerHasTakenPassedCards.cards());
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
        heartsWriteApi.passCards(cardsDealt.gameId(), playerId, cardsToPass);
    }

    private boolean shouldPassCards() {
        return (roundNumber - 1) % 4 != 3;
    }

    private void updateHand(CardsDealt cardsDealt) {
        List<Card> playerHand = getCardsForPlayer(cardsDealt);
        hand.addAll(playerHand);
    }

    private List<Card> getCardsForPlayer(CardsDealt cardsDealt) {
        Map<PlayerId, List<Card>> playerHands = cardsDealt.playerHands();
        return playerHands.get(playerId);
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
        Card cardToPlay = playerInputHandler.chooseCardToPlay(hand, playableCards);
        heartsWriteApi.playCard(gameId, playerId, cardToPlay);
    }

}
