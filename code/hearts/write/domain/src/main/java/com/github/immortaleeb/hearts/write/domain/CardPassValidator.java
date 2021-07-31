package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hears.common.shared.Card;
import com.github.immortaleeb.hearts.write.shared.CardsNotInHand;
import com.github.immortaleeb.hearts.write.shared.IncorrectNumberOfCardsPassed;
import com.github.immortaleeb.hearts.write.shared.PlayerAlreadyPassedCards;

import java.util.List;

class CardPassValidator {

    private final int numberOfCardsToPass;

    public CardPassValidator(int numberOfCardsToPass) {
        this.numberOfCardsToPass = numberOfCardsToPass;
    }

    public void verifyPassIsValid(Player fromPlayer, List<Card> cards) {
        if (cards.size() != numberOfCardsToPass) {
            throw new IncorrectNumberOfCardsPassed();
        }

        if (!fromPlayer.hand().contains(cards)) {
            throw new CardsNotInHand();
        }

        if (fromPlayer.hasPassedCards()) {
            throw new PlayerAlreadyPassedCards();
        }
    }

}
