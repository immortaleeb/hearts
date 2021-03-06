package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.Comparator;
import java.util.function.Predicate;

class PlayedCard {

    private final Card card;
    private final PlayerId playedBy;

    public PlayedCard(Card card, PlayerId playedBy) {
        this.card = card;
        this.playedBy = playedBy;
    }

    public Suite suite() {
        return card.suite();
    }

    public Card card() {
        return card;
    }

    public PlayerId playedBy() {
        return playedBy;
    }

    public static Predicate<PlayedCard> matchingSuite(Suite suite) {
        Predicate<Card> cardPredicate = Card.matchingSuite(suite);
        return playedCard -> cardPredicate.test(playedCard.card);
    }

    public static Comparator<PlayedCard> compareByRank() {
        Comparator<Card> cardComparator = Card.compareByRank();
        return (card1, card2) -> cardComparator.compare(card1.card, card2.card);
    }
}
