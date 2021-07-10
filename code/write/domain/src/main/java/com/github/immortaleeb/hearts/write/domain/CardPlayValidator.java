package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.CardNotInHand;
import com.github.immortaleeb.hearts.write.shared.Suite;

class CardPlayValidator {

    private final Card openingCard;

    public CardPlayValidator(Card openingCard) {
        this.openingCard = openingCard;
    }

    public ValidationResult validateCardPlay(Trick currentTrick, Hand playerHand, Card cardToBePlayed) {
        if (!playerHand.contains(cardToBePlayed)) {
            throw new CardNotInHand();
        }

        if (playerHand.contains(openingCard) && !openingCard.equals(cardToBePlayed)) {
            return ValidationResult.failed("You must open with the two of clubs");
        }

        if (currentTrick.hasCards()) {
            Suite trickSuite = currentTrick.suite();

            boolean playerCanFollowSuite = playerHand.anyCard(Card.matchingSuite(trickSuite));
            boolean playerFollowsSuite = trickSuite == cardToBePlayed.suite();
            boolean isValidPlay = playerFollowsSuite || !playerCanFollowSuite;

            if (!isValidPlay) {
                return  ValidationResult.failed("Player must follow suite when possible");
            }
        }

        return ValidationResult.success();
    }

}
