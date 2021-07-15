package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class StdinPlayerInputHandler implements PlayerInputHandler {

    private static final int CARDS_TO_PASS = 3;

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
        List<Card> passableCards = new ArrayList<>(hand);
        List<Card> cardsToPass = new ArrayList<>();

        System.out.println("Select " + CARDS_TO_PASS + " cards to pass");

        for (int i = 0; i < CARDS_TO_PASS; i++) {
            Card selectedCard = promptSelectCard(passableCards, "Please choose a card to pass: ");
            passableCards.remove(selectedCard);
            cardsToPass.add(selectedCard);
        }

        return cardsToPass;
    }

    @Override
    public Card chooseCardToPlay(List<Card> playableCards) {
        System.out.println("Which card will you play?");
        return promptSelectCard(new ArrayList<>(playableCards), "Please choose a card to play: ");
    }

    private Card promptSelectCard(List<Card> hand, String prompt) {
        hand.sort(Card.compareBySuiteAndRank());
        printCards(hand);

        System.out.print(prompt);
        int selectedCardNumber = readNumber(number -> number > 0 && number <= hand.size());

        Card selectedCard = hand.get(selectedCardNumber - 1);
        System.out.println("Selected " + selectedCard);

        return selectedCard;
    }

    private void printCards(List<Card> cards) {
        for (int i = 1; i <= cards.size(); i++) {
            Card card = cards.get(i - 1);
            System.out.println(i + ". " + card);
        }
    }

    private int readNumber(Predicate<Integer> validateNumber) {
        return readValue(Integer::parseInt, validateNumber);
    }

    private <T> T readValue(Function<String, T> mapper, Predicate<T> validate) {
        Scanner scanner = new Scanner(System.in);

        T value = mapper.apply(scanner.nextLine());
        if (validate.test(value)) {
            return value;
        }

        do {
            System.out.println("Please enter valid input!");
            value = mapper.apply(scanner.nextLine());
        } while (!validate.test(value));

        return value;
    }

}
