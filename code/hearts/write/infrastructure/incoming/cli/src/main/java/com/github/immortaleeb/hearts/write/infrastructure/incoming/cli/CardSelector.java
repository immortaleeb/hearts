package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CardSelector {

    private final int numberOfCards;
    private final String prompt;

    private final StdinReader stdinReader;
    private final CardPrinter printer = new CardPrinter();

    CardSelector(StdinReader stdinReader, int numberOfCards, String prompt) {
        this.stdinReader = stdinReader;

        this.numberOfCards = numberOfCards;
        this.prompt = prompt;
    }

    public Card selectSingleCard(List<Card> hand, List<Card> validCards) {
        return selectCards(hand, validCards).get(0);
    }

    public List<Card> selectCards(List<Card> hand, List<Card> validCards) {
        List<Card> selectedCards = new ArrayList<>();

        printer.printCards(hand, validCards);

        while (selectedCards.size() < numberOfCards) {
            int cardNumber = promptCardNumber();

            if (cardNumber < 1 || cardNumber > hand.size()) {
                System.out.println("Please enter a number between 1 and " + hand.size());
                continue;
            }

            Card card = hand.get(cardNumber - 1);
            if (selectedCards.contains(card)) {
                System.out.println("You cannot select the same card twice!");
            } else if (!validCards.contains(card)) {
                System.out.println("This card cannot be played at this moment.");
            } else {
                selectedCards.add(card);
            }
        }

        return selectedCards;
    }

    private int promptCardNumber() {
        Optional<Integer> userInput = tryPromptCardNumber();

        while (userInput.isEmpty()) {
            System.out.println("Not a valid card number, try again!");
            userInput = tryPromptCardNumber();
        }

        return userInput.get();
    }

    private Optional<Integer> tryPromptCardNumber() {
        System.out.print(prompt);
        return stdinReader.readInt();
    }

}
