package com.github.immortaleeb.hearts.write.infrastructure.incoming.cli;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.List;

class CardPrinter {

    public void printCards(List<Card> cards, List<Card> highlightedCards) {
        for (int i = 1; i <= cards.size(); i++) {
            Card card = cards.get(i - 1);

            String highlight = "";
            if (highlightedCards.contains(card)) {
                highlight = "*";
            }
            System.out.println(i + ". " + highlight + card);
        }
    }

}
