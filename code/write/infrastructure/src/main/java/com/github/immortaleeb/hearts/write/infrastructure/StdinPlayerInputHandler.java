package com.github.immortaleeb.hearts.write.infrastructure;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class StdinPlayerInputHandler implements PlayerInputHandler {

    private static final int CARDS_TO_PASS = 3;

    @Override
    public List<Card> chooseCardsToPass(List<Card> hand) {
        List<Card> passableCards = new ArrayList<>(hand);
        List<Card> cardsToPass = new ArrayList<>();

        for (int i = 0; i < CARDS_TO_PASS; i++) {
            Card selectedCard = promptSelectCard(passableCards, "Please choose a card to pass: ");
            passableCards.remove(selectedCard);
            cardsToPass.add(selectedCard);
        }

        return cardsToPass;
    }

    @Override
    public Card chooseCardToPlay(List<Card> playableCards) {
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
