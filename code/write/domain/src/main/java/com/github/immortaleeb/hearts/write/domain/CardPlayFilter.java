package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;

import java.util.List;
import java.util.stream.Collectors;

class CardPlayFilter {

    private final CardPlayValidator cardPlayValidator;

    CardPlayFilter(CardPlayValidator cardPlayValidator) {
        this.cardPlayValidator = cardPlayValidator;
    }

    public List<Card> filterValidCardsToPlayFromHand(Trick currentTrick, Hand playerHand) {
        List<Card> validCardsToPlay = playerHand.cards()
                .stream()
                .filter(card -> cardPlayValidator.validateCardPlay(currentTrick, playerHand, card).isSuccess())
                .collect(Collectors.toList());

        if (validCardsToPlay.isEmpty()) {
            return playerHand.cards();
        }

        return validCardsToPlay;
    }

}
