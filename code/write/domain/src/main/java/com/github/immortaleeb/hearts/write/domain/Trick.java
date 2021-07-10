package com.github.immortaleeb.hearts.write.domain;

import com.github.immortaleeb.hearts.write.shared.Card;
import com.github.immortaleeb.hearts.write.shared.PlayerId;
import com.github.immortaleeb.hearts.write.shared.Suite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class Trick {

    private final List<PlayedCard> playedCards;

    public Trick(List<PlayedCard> playedCards) {
        this.playedCards = playedCards;
    }

    public Suite suite() {
        return playedCards.get(0).suite();
    }

    public int numberOfPlayedCards() {
        return playedCards.size();
    }

    public boolean hasPlayedCard(PlayerId playerId) {
        return playedCards.stream()
                .anyMatch(playedCard -> playedCard.playedBy().equals(playerId));
    }

    public PlayerId winner() {
        return winningCard().playedBy();
    }

    private PlayedCard winningCard() {
        return playedCards.stream()
                .filter(PlayedCard.matchingSuite(suite()))
                .max(PlayedCard.compareByRank())
                .get();
    }

    public Stream<Card> cards() {
        return playedCards.stream()
                .map(PlayedCard::card);
    }

    public void play(Card card, PlayerId playedBy) {
        playedCards.add(new PlayedCard(card, playedBy));
    }

    public boolean hasCards() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return playedCards.isEmpty();
    }

    public static Trick empty() {
        return new Trick(new ArrayList<>());
    }

}
