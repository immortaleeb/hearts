package com.github.immortaleeb.hearts.domain;

import com.github.immortaleeb.hearts.shared.Card;
import com.github.immortaleeb.hearts.shared.PlayerId;

import java.util.List;
import java.util.stream.Collectors;

class Player {

    private final PlayerId id;
    private final Hand hand;
    private boolean passedCards;

    private Player(PlayerId id) {
        this.id = id;
        this.hand = Hand.empty();
    }

    public PlayerId id() {
        return id;
    }

    public Hand hand() {
        return hand;
    }

    public boolean hasPassedCards() {
        return passedCards;
    }

    public void takePassedCardsFrom(Table table) {
        List<Card> passedCards = table.cardsPassedTo(id);

        table.takeCardsPassedTo(id);
        hand.receive(passedCards);
    }

    public void passCardsTo(PlayerId toPlayer, List<Card> cards, Table table) {
        hand.take(cards);
        table.passCardsTo(toPlayer, cards);
        passedCards = true;
    }

    public void reset() {
        this.passedCards = false;
    }

    public static List<Player> listOf(List<PlayerId> playerIds) {
        return playerIds.stream()
                .map(Player::new)
                .collect(Collectors.toList());
    }
}
