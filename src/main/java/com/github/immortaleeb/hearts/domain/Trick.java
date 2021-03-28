package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.NotPlayersTurn;
import com.github.immortaleeb.hearts.shared.PlayerId;
import com.github.immortaleeb.hearts.shared.Suite;

import java.util.ArrayList;
import java.util.List;

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

    public PlayerId decideWinner() {
        Suite trickSuite = playedCards.get(0).suite();

        PlayedCard winningCard = playedCards.stream()
                .filter(PlayedCard.matchingSuite(trickSuite))
                .max(PlayedCard.compareByRank())
                .get();

        return winningCard.playedBy();
    }

    public void play(Card card, PlayerId playedBy) {
        playedCards.add(new PlayedCard(card, playedBy));
    }

    public boolean isEmpty() {
        return playedCards.isEmpty();
    }

    public static Trick empty() {
        return new Trick(new ArrayList<>());
    }

}
