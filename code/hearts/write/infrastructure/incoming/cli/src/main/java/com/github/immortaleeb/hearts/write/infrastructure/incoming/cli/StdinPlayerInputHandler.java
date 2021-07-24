package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.*;

public class StdinPlayerInputHandler implements PlayerInputHandler {

    private static final int CARDS_TO_PASS = 3;

    private final StdinReader stdinReader = new StdinReader();
    private final CardSelector cardsToPassSelector = new CardSelector(stdinReader, 3, "Which card do you want to pass? ");
    private final CardSelector cardToPlaySelector = new CardSelector(stdinReader, 1, "Which card do you want to play? ");

    @Override
    public void showDealtCards(List<Card> hand) {
        System.out.println("Cards have been dealt");
    }

    @Override
    public void showReceivedCards(PlayerId fromPlayer, List<Card> receivedCards) {
        System.out.printf("Received cards from player %s: %s\n", fromPlayer, receivedCards);
    }

    @Override
    public void showPlayedCard(PlayerId playedBy, Card playedCard) {
        System.out.printf("%s played %s\n", playedBy, playedCard);
    }

    @Override
    public void showPlayerWonTrick(PlayerId trickWinner) {
        System.out.println(trickWinner + " won the trick!");
        System.out.println();
    }

    @Override
    public void showRoundEnded(Map<PlayerId, Integer> roundScores) {
        System.out.println("Round ended with the following scores:");
        printScores(roundScores);
    }

    @Override
    public void showGameEnded(Map<PlayerId, Integer> scores) {
        System.out.println("Game ended with the following scores:");
        printScores(scores);
    }

    private void printScores(Map<PlayerId, Integer> scores) {
        scores.forEach((player, score) -> System.out.printf("%s -> %d\n", player, score));
        System.out.println();
    }

    @Override
    public List<Card> chooseCardsToPass(List<Card> hand) {
        System.out.println("Select " + CARDS_TO_PASS + " cards to pass");
        return cardsToPassSelector.selectCards(sortedHand(hand), hand);
    }

    @Override
    public Card chooseCardToPlay(List<Card> hand, List<Card> playableCards) {
        System.out.println("Your turn to play a card");
        return cardToPlaySelector.selectSingleCard(sortedHand(hand), playableCards);
    }

    private List<Card> sortedHand(List<Card> hand) {
        List<Card> sortedHand = new ArrayList<>(hand);
        sortedHand.sort(Card.compareBySuiteAndRank());
        return sortedHand;
    }

}
